# Use by Jitpack

[![JitPack](https://jitpack.io/v/devezhao/persist4j.svg)](https://jitpack.io/#devezhao/persist4j)
[![JavaDoc](https://img.shields.io/badge/java-doc-red.svg)](https://devezhao.github.io/persist4j/index.html)

## Maven

```
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.github.devezhao</groupId>
    <artifactId>persist4j</artifactId>
    <version>LAST_VERSION</version>
  </dependency>
</dependencies>
```

## 如何使用

### 基础操作对象

使用 [persist4j](https://github.com/devezhao/persist4j) 首先需要了解两个基础对象 `Record` 和 `Query`。`Record` 是一个 Map 实现，他承担了 DAO 职责，而 `Query` 是查询入口，是日常编码中使用最为频繁的类。

#### Record 对象

`Record` 包含了诸多 setter 和 getter 方法，在实现动态化 DAO 职责的同时，还具备对不同数据（字段）类型进行校验的能力。以下是一个基本的使用示例。

```
// 获取数据管理对象
PersistManagerFactory PMF = ...
PersistManager PM = PMF.createPersistManager();

Record record = new StandardRecord()
record.setString("FieldName", "FieldValue");

PM.create(record);
```

> 通常你可以将上述代码封装为一个工具方法进行使用

#### Query 对象

`Query` 的使用主要是对 persist4j 独有的 AJQL （Auto Join Query Language）语句进行理解。AJQL 是 Like-SQL 语法，因此学习和理解起来并不困难。

```
// 获取数据管理对象
PersistManagerFactory PMF = ...
PersistManager PM = PMF.createPersistManager();

String ajql = "select Field1, ReferenceField1.Field2 from Table1 where Field2 = ?";
Record found = PM.createQuery(ajql).setParameter(1, "SomeValue").unique();
```

> 通常你可以将上述代码封装为一个工具方法进行使用

请注意上例中 select 子句中的 `ReferenceField1.Field2` 字段，`ReferenceField1` 是一个引用型字段，因此可以使用 `.` 进行（自动）关联查询（其中 `Field2` 是关联表中的字段）。如果 `Field2` 也是一个引用字段，那么可以继续进行关联查询。

可以看出，通过 AJQL 可以大大简化我们日常查询 SQL 中的表关联操作，同时这也正是 AJQL 名称的由来。

#### Query 自动装箱

经由 `Query` 查询出来的数据并不一定是数据库底层的数据类型，例如 `ID` 主键虽然使用 `varchar` 存储，但是查询后会自动转换为 `ID` 对象（其他字段同理）。这是因为我们会根据元数据的字段定义将其自动装箱，而无需你手动二次处理，同时也保证了数据的严谨性。

### 元数据

动态元数据是支撑 [persist4j](https://github.com/devezhao/persist4j) 的核心，使用 XML 配置，也可以将其存储在数据库中动态加载。无论何种方式没有本质区别，仅在与来源不同。

```
<entity name="ProjectConfig" type-code="050" description="项目配置" queryable="false">
  <field name="configId" type="primary" />
  <field name="projectName" type="string" max-length="100" nullable="false" />
  <field name="projectCode" type="string" max-length="10" nullable="false" />
  <field name="comments" type="string" max-length="300" />
  <field name="principal" type="reference" ref-entity="User" />
  <field name="members" type="string" max-length="420" default-value="ALL" />
  <field name="showConfig" type="text" max-length="3000" />
</entity>
```

以上示例片段来自 [rebuild](https://github.com/getrebuild/rebuild/) 项目，他定义了一个 `ProjectConfig` 实体及其所拥有的字段。关于各配置项的说明，请参考 [metadata.dtd](https://github.com/devezhao/persist4j/blob/master/src/main/resources/metadata.dtd) 。

元数据的配置最终会被映射为 `Entity` 和 `Field` 对象，可以对元数据进行操作。

#### 主键 ID

`ID` 对象即主键对象，其生成规则为 `([0-9]{3}-[0-9a-h]{16})`，其中前 3 位为实体码（不足 3 位前面补 0），其后为 `-` 分隔符，最后为 16 位随机 Hash 码（不区分大小写）。可以看出，通过 ID 我们可以识别其属于哪个实体。

### 最佳实践

我们建议你将 [persist4j](https://github.com/devezhao/persist4j) 与 Spring 配合使用，可以大大简化编码。当然，这不是必须的。通过参考 [rebuild](https://github.com/getrebuild/rebuild/) 项目你可以进行更多的了解。
