# persist4j 全代码库审查报告

审查日期 2026-09-22。审查范围 `src/main/java` 下全部 105 个主源码文件，排除 3 个 ANTLR 2.7.7 生成文件（AjQLLexer、AjQLParser、AjQLParserTokenTypes）和 docs/ 下的 JavaDoc。未执行构建与测试，问题判定全部基于源码静态分析。

## 一、审查范围与总体结论

总体印象。这个框架的分层设计是清晰的，root 接口、metadata 元数据、dialect 类型体系、engine JDBC 执行、query AJQL 编译、record 记录构造，各包职责边界干净。exception 包模仿 Hibernate 的 SQLException 转换体系做得规范。但静态审查发现了约 40 项问题，其中 3 个问题链需要优先处理，一是 ID 校验不严拼接形成的 SQL 注入链，二是 OracleDialect 分页 SQL 生成完全损坏，三是 equals/hashCode 反模式在 6 处系统性存在。另有两处数据损坏缺陷（Clob 读取丢换行、二进制写入截断）和一处元数据刷新失败后状态损坏的问题。

## 二、安全

### 2.1 ID 校验不严与 SQL 拼接形成的注入链（高）

`src/main/java/cn/devezhao/persist4j/engine/ID.java` 的 `isId()` 只检查总长 19、前 3 位数字、第 4 位是连字符、不含空格，不校验后 15 位的字符集。形如 `001-abcde'fghij` 的字符串可以通过校验进入 ID。而 `PersistManagerImpl.delete()` 用 MessageFormat 把 `id.toLiteral()` 直接拼进 delete 语句，AJQL 编译器也接受字面 ID。两者叠加形成注入链。

修复方向。isId 改用正则 `^[0-9]{3}-[0-9a-z_-]{16}$`。校验侧可以比生成侧宽松（生成只产生 [0-9a-h]），后 16 位放宽为字母数字加减下划线，兼容历史或外部数据，同时仍能拦住引号等注入字符。delete 系列改用 PreparedStatement 参数绑定。

### 2.2 AJQL 字符串字面量无转义拼接（高）

`src/main/java/cn/devezhao/persist4j/query/compiler/QueryCompiler.java` 在多处把 QUOTED_STRING 的文本用 `append('\'').append(text).append('\'')` 直接拼进 SQL（数值转换分支、字符串分支、like 分支、IN 集合分支）。编译后的 SQL 走 `executeQuery(sql, params)` 而非参数绑定，含单引号的用户输入可直接破坏 SQL 结构。`compileMatchClause` 的 `String.format("match (%s) against ('%s'...)")` 以及 GROUP_CONCAT 的 separator 同样未转义。

修复方向。对字面文本做 `'` 到 `''` 的转义，或改为占位符参数绑定。match against 建议直接禁止字面量形式，强制走参数。

### 2.3 XXE 与解析器线程安全（中）

`src/main/java/cn/devezhao/persist4j/util/XmlHelper.java` 的 SAXReader 未设置 `disallow-doctype-decl` 等防护特性，解析不可信 XML 时存在 XXE 风险。dom4j 1.x 默认配置下外部实体是可解析的。另外 saxReader、domReader 是实例字段跨调用共享，SAXReader 本身非线程安全，并发调用 read 可能产生错乱结果。`read(in, null)` 路径没有 EntityResolver。

修复方向。工厂方法里每次新建解析器并设置 `http://apache.org/xml/features/disallow-doctype-decl` 为 true，同时禁 external-general-entities 与 external-parameter-entities。

### 2.4 SchemaExport 两处小问题（低）

`src/main/java/cn/devezhao/persist4j/util/support/SchemaExport.java` 用 `//entity[@name='" + entity.getName() + "']` 拼 XPath。输入来自受信的元数据文件，实际风险低，但作为防御性编程应改用变量绑定。finally 里调用 `SqlHelper.close(connection)` 关闭了外部传入的连接，违反最小意外原则，由调用方管理更合适。

## 三、正确性，高危

### 3.1 Oracle 分页 SQL 生成完全损坏

`src/main/java/cn/devezhao/persist4j/dialect/OracleDialect.java` 的 `insertLimit()` 在 offset > 0 分支使用了

```
select * from ( select row_.*, ROWNUM rownum_ from ( %s ) where rownum_ <= ? and rownum_ > ?
```

格式串只有 1 个 `%s`，String.format 却传了 3 个参数。两个 `?` 不会被替换，执行时对应参数无人绑定，必然报错。且从 `select * from (` 起括号不闭合，SQL 本身就是残缺的。结论是 Oracle 方言的分页查询路径完全不可用，说明这条路径从未被测试覆盖过。

修复方向。改用规范的三层嵌套写法，外层 `where rownum_ > ? and rownum_ <= ?`，并保证括号配对。顺带修正 FieldType.LONG 映射到 Oracle LONG（变长字符类型而非整数）的错误，应映射 NUMBER。

### 3.2 Dialect 未注册 DATETIME 类型

`src/main/java/cn/devezhao/persist4j/dialect/Dialect.java` 42-67 行构造器注册了 18 个 FieldType（PRIMARY、REFERENCE、ANY_REFERENCE、REFERENCE_LIST、INT、SMALL_INT、DOUBLE、DECIMAL、LONG、CHAR、STRING、TEXT、DATE、TIMESTAMP、TIME、BOOL、NTEXT、BINARY），唯独没有 DATETIME。而 `FieldType.java` 53 行定义了 DATETIME（mask 10022），`editor/DateTimeEditor.java` 也基于它实现（2024-12 新增），MySQL5Dialect 还注册了对应的 columnType。但 `ConfigurationMetadataFactory` 解析元数据 XML 走 `dialect.getFieldType(shortName)` 短名查找，遍历的正是构造器注册的列表。结果是元数据里声明 `type="datetime"` 的字段必然抛出 "No field type defined by short name" 异常。新增类型时漏改基类注册，属于典型的修改不同步缺陷。

修复方向。在 Dialect 构造器补一行 `registerFieldType(FieldType.DATETIME)`。

### 3.3 equals/hashCode 反模式，6 处系统性存在

以下 6 个类全部用 `hashCode() == obj.hashCode()` 代替字段值比较：

| 位置 | 后果 |
|---|---|
| `engine/ID.java` | hash 碰撞时两个不同 ID 被判等，影响缓存与去重语义 |
| `query/compiler/JoinTree.java` JoinNode | childJoins 是 LinkedHashSet，碰撞导致 join 节点被错误去重 |
| `metadata/impl/EntityImpl.java` | 实体误判相等 |
| `metadata/impl/FieldImpl.java` | 字段误判相等 |
| `util/CaseInsensitiveMap.java` CIEntry | Map.Entry 语义错误 |
| `query/compiler/ParameterItem.java` | 参数项误判相等 |

配合下一项 FieldType.hashCode 的错误实现，问题被放大。

修复方向。统一改为字段比较（ID 比 literal，Entity 比 entityCode，Field 比 ownEntity 加 name），equals 与 hashCode 配套重写。

### 3.4 FieldType.hashCode 违反契约

`src/main/java/cn/devezhao/persist4j/dialect/FieldType.java` 的 hashCode 写成 `return super.hashCode() >>> getMask()`。super.hashCode 是 identity hash，右移 mask 位（超过 32 时 JLS 按 mod 32 截断），得到的是与对象身份相关的不确定值。同一逻辑类型在不同 JVM 里 hashCode 不同，放进 HashSet 或做 equals 判断时行为不可预测。

修复方向。改为 `return getMask()` 或基于 name 的稳定哈希。

### 3.5 元数据刷新失败后状态损坏

`src/main/java/cn/devezhao/persist4j/metadata/impl/ConfigurationMetadataFactory.java` 的 `refresh()` 先 clear 旧 map 再 build，build 抛异常时不会回滚，元数据停留为空，finally 只负责重置 refreshLocked 标志。此后所有 getEntity 调用抛 MissingMetaExcetion，故障从“刷新失败”恶化为“服务不可用”。另外 `waitForRefreshLocked()` 超时后继续读半构建状态的 map。

修复方向。先在局部变量构建新 map，成功后一次性原子替换引用，失败时保留旧元数据并抛出异常。

### 3.6 同名命名参数被覆盖

QueryCompiler 编译 AJQL 时把命名参数收进 Map，同一参数名出现两次时（如 `where a.x = :p or b.y = :p`）后值覆盖前值。SQL 里有两个 `?`，Map 里只剩一个条目，执行时必有一个占位符未绑定而抛 SQLException。

修复方向。inParameters 改为 List 并按出现顺序绑定，或编译期对重复参数名报错。

### 3.7 建表默认值生成畸形 SQL

`src/main/java/cn/devezhao/persist4j/util/support/Table.java` 192-199 行的 `generateFieldDDL` 在 canNull 已含 default 的分支写的是

```java
canNull = canNull.split("default")[0];
canNull += " " + defaultValue.replaceAll("'", "") + "'";
```

拼接结果缺少开头单引号，生成形如 `not null 0'` 的残缺片段。触发条件是 TIMESTAMP 或 DATE 的 not null 字段同时配置了 defaultValue。另外 216 行自增列唯一索引硬编码 MySQL 反引号而非 `dialect.quote`，在非 MySQL 方言下生成错误 SQL。

修复方向。统一走 else 分支的 `default '...'` 拼接逻辑，索引 DDL 使用 dialect.quote。

### 3.8 DATE 聚合查询读值抛异常

`src/main/java/cn/devezhao/persist4j/query/AjqlResultImpl.java` 的 `readRow` 对 DATE 聚合列取 `FieldType.INT.getFieldEditor()` 读取。IntEditor.get 内部调 `ObjectUtils.toInt()`，而 `DATE()` 聚合返回的是 java.sql.Date，转换抛 NumberFormatException。凡是 AJQL 里对日期字段做聚合的查询都会失败。

修复方向。DATE 聚合列改用 DateEditor 读取。

## 四、数据质量

### 4.1 Clob 读取丢失换行

`src/main/java/cn/devezhao/persist4j/dialect/editor/NTextEditor.java` 的 `read2String` 用 BufferedReader.readLine 逐行拼 StringBuilder，行间换行符没有补回，读出的大文本内容被压成一行。这是静默的数据损坏。同类问题，set 方法在 value 为 null 时调用 `value.toString()` 抛 NPE，异常分支返回原 Clob 对象，导入了 hutool 的 IoUtil 但从未使用。

修复方向。用 `clob.getSubString(1, (int) clob.length())` 整体读取，换行天然保留。

### 4.2 二进制写入截断

`src/main/java/cn/devezhao/persist4j/dialect/editor/BinaryEditor.java` 的 `set` 用 `stream.available()` 当流长度传给 setBinaryStream。available 的语义是“当前可无阻塞读取的字节数”，不是总长。对文件流或网络流，实际写入往往只是前一段数据，造成静默截断。顺带一提，方法里还有 `System.out.println("## STREAM IS EMPTY")` 的调试输出和未使用的 ByteUtils 导入。

修复方向。先读入 byte[] 再 setBytes，或用确定长度的包装。

### 4.3 级联删除无事务保护

`src/main/java/cn/devezhao/persist4j/engine/PersistManagerImpl.java` 的 `delete(ID)` 级联删除时逐条执行 SQL，没有显式事务。自动提交模式下每条独立提交，中途失败（外键约束、连接中断）会留下删除了一半的父子数据。配合 2.1 的 ID 拼接，delete 方法是本项目风险最集中的入口。

修复方向。级联删除包在单个事务里，失败整体回滚。

## 五、健壮性

以下问题按触发概率排序，均为防御性缺陷，不改变正常路径行为。

### 5.1 空值与类型边界

- `util/CaseInsensitiveMap.java` 的 convertKey 对 null key 直接 `toString()` 抛 NPE，get/put/remove/containsKey 传 null 全部触发。`EntityImpl.getField(null)` 的调用链随之中断。CIEntry.setValue 返回新值而非旧值，违反 Map.Entry 契约，keySet/entrySet 返回副本而非视图。
- `dialect/editor/BoolEditor.java` 与 `CharEditor.java` 的 get 对空字符串调 `charAt(0)` 越界，BoolEditor.set 对 null Boolean 拆箱 NPE。`TimeEditor.java`、`TimestampEditor.java` 的 set 在 value 为 null 时 `value.getClass()` NPE，else 分支强转 (Time)/(Timestamp) 传错类型抛 CCE。`PrimaryEditor.java` set 强转 (ID)，传 String 抛 CCE。`ReferenceListEditor.java` get 对空段调 ID.valueOf 抛异常。
- `util/support/SecurityPreferencesConfigurer.java` 的 afterLoad 不判 null 直接 `dbUrl.startsWith()`，配置缺 db.url 即 NPE，且 URL 无查询串时用 `&` 而非 `?` 拼接。
- `exception/SqlExceptionConverter.java` 的 convert 用 `getLocalizedMessage().contains(...)` 判类型，消息为 null 时 NPE。DATA_CATEGORIES 与 CONNECTION_CATEGORIES 两个集合定义后从未使用。
- `engine/StandardRecord.java` 的 getIDArray、checkReferenceValue、getTime 未做类型检查直接强转，反序列化后 entity 为 null 时 getPrimary NPE，clone 对数组值是浅拷贝。
- `record/RecordVisitor.java` 的 clearNumber 对非数字静默存 "0" 而非报错，tryParseTime 的 LocalTime.ofSecondOfDay 在 try 块外，越界直接抛出。

### 5.2 资源与并发

- `engine/JdbcSupport.java` 的 nativeQuery 没有 finally 块，查询抛异常时 stmt 与 connect 均泄漏。这是唯一确认的连接泄漏点，长期运行会耗尽连接池。
- `query/AjqlResultImpl.java` 把全部结果行缓存在 LinkedList（dataCache）。超大结果集直接 OOM，且 reset 前的查询不释放旧缓存。QueryHelper 的分页先 reset 再 setLimit 的时序已验证无误，问题只在大结果集无上限。
- `query/SlowLogger.java` 的 ThreadLocal 在异常路径不清理，线程池复用线程时串日志。`query/NativeQueryImpl.java` 的 parameterTypes 用 `e.getKey()-1` 做下标，参数名不含序号稀疏时数组越界。
- `engine/WeakIDGenerator.java` 生成的弱 ID 不含 IP 或 JVM 标识，同库多 JVM 部署时存在主键冲突可能。仅单机场景安全。
- `metadata/impl/EntityImpl.java` 的 getNameField 有副作用，会写 nameFieldName 缓存字段，多线程并发首调不安全。getFields() 基于 HashMap 无序，而 Table.generateDDL 用 getFieldSorted() 保持 XML 顺序，两个入口行为不一致，建表和运行时字段遍历顺序可能不同。

### 5.3 设计与死代码

- `record/XmlBean.java` 是空壳类，构造器与 addValue 均空实现，调用方拿到的对象毫无作用。
- getDetialEntities 拼写错误（应为 Detail）已经固化到 `Entity.java` 94 行公共接口，v1.7 之后改不了了。MissingMetaExcetion 类名同样拼错（Excetion）。ConfigurationMetadataFactory 里还有 "mianEntity" 拼写和 containsEntity(int) 用 containsValue 做 O(n) 查找。
- `query/AjqlQuery.java` 的 unique 与 NativeQueryImpl.unique 修改 query 自身状态（分页参数），属于有副作用的读操作。AjqlResultImpl.unique 不校验结果唯一性，重了不报错。
- `dialect/MySQL5Dialect.java` 的 getClosedQuote 返回 QUOTED[0]、getStartQuote 返回 QUOTED[1]，语义互换。因两字符同为反引号而无实际影响，但语义上是对的应该是反的。
- Dialect.getFieldType 短名查找是 O(n) 遍历，元数据加载阶段可接受，不值得改。`query/QueryedRecord.java` 的 setObject 吞 FieldValueException 只记 warn，调用方无感知。

## 六、构建与依赖

pom.xml 声明了 Spring 4.3.30、Druid 1.2.10、MySQL Connector 5.1.49、H2 2.1.214 和 commons 1.3.13，但以下依赖实际被源码直接 import，全部来自传递依赖，未显式声明：

| 依赖 | 使用位置 |
|---|---|
| fastjson | BaseMetaObject、多处 Record/JSON 处理 |
| hutool | NTextEditor（导入未用） |
| dom4j | XmlHelper、ConfigurationMetadataFactory、Table |
| commons-lang 2.x | Table、QueryHelper 等处 StringUtils |
| commons-logging | Dialect 等处 Log/LogFactory |
| commons-collections4 | CaseInsensitiveMap 基类 |

风险有两层。一是一旦上游（主要是 commons 1.3.13）调整传递依赖，本项目直接编译失败。二是 fastjson 版本不受控，老版本存在 autoType 反序列化 RCE 的历史漏洞，作为被下游大量引用的基础库，版本应当锁定。MySQL Connector 停留在 5.1.49（2013 年分支）也建议升级到 8.x。

## 七、修复优先级建议

### P0，直接影响功能正确性与安全

1. OracleDialect.insertLimit 重写（3.1），Oracle 分页当前完全不可用
2. Dialect 补注册 DATETIME（3.2），一行改动
3. ID.isId 改正则校验，delete 改参数绑定（2.1）
4. NTextEditor.read2String 改整体读取（4.1），BinaryEditor.set 改定长写入（4.2）

### P1，数据一致性与稳定性

5. ConfigurationMetadataFactory.refresh 改原子替换（3.5）
6. 级联删除包事务（4.3）
7. JdbcSupport.nativeQuery 补 finally（5.2）
8. AJQL 字面量转义（2.2）、XmlHelper XXE 防护（2.3）
9. Table.generateFieldDDL 默认值引号（3.7）、AjqlResultImpl DATE 聚合（3.8）

### P2，代码卫生

10. 6 处 equals/hashCode 重写（3.3、3.4）
11. 各 editor 空值与类型边界防御
12. pom 显式声明全部直接依赖并锁定 fastjson 版本
13. 清理死代码（XmlBean、未用导入、调试输出、拼写遗留）

## 八、结语

审查范围内的代码整体处于“结构良好但细节失修”的状态。核心 API 设计经受住了时间考验，问题集中在三条线上，安全防线（ID 校验、字面量转义、XXE）从未真正建立，方言差异处理缺少 Oracle 侧的实测覆盖，equals/hashCode 这类基础契约存在系统性的错误写法。P0 的 4 项里有两项（Oracle 分页、DATETIME 注册）是小改动大收益，建议优先处理。
