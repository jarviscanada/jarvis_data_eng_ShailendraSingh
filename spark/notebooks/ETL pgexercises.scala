// Databricks notebook source
val bookingsDF = spark.read
  .format("csv")
  .option("header", "true")
  .option("inferSchema", "true")
  .load("dbfs:/FileStore/bookings.csv")

  display(bookingsDF)

// COMMAND ----------

spark.sql("DROP TABLE IF EXISTS bookings");
bookingsDF.write.saveAsTable("bookings");

// COMMAND ----------

// MAGIC %sql
// MAGIC SELECT * FROM bookings

// COMMAND ----------

val facilitiesDF = spark.read
  .format("csv")
  .option("header", "true")
  .option("inferSchema", "true")
  .load("dbfs:/FileStore/facilities.csv")

  display(facilitiesDF)

// COMMAND ----------

spark.sql("DROP TABLE IF EXISTS facilities");
facilitiesDF.write.saveAsTable("facilities");

// COMMAND ----------

// MAGIC %sql
// MAGIC SELECT * FROM facilities

// COMMAND ----------

val membersDF = spark.read
  .format("csv")
  .option("header", "true")
  .option("inferSchema", "true")
  .load("dbfs:/FileStore/members.csv")

  display(membersDF)

// COMMAND ----------

spark.sql("DROP TABLE IF EXISTS members");
facilitiesDF.write.saveAsTable("members");

// COMMAND ----------

// MAGIC %sql
// MAGIC SELECT * FROM members
