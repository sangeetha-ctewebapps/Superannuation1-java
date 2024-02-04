//package com.lic.epgs.quotation.config;
//
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.sql.SparkSession;
//import org.apache.spark.sql.api.java.UDF0;
//import org.apache.spark.sql.api.java.UDF2;
//import org.apache.spark.sql.api.java.UDF3;
//import org.apache.spark.sql.types.DataTypes;
//import org.springframework.beans.factory.annotation.Value;
//
////@Configuration
//public class SparkConfig {
//
//	@Value("${spark.app.name}")
//	private String appName;
//
//	@Value("${spark.master}")
//	private String masterUri;
//
//	//@Bean
//	public SparkConf sparkConf() {
//		return new SparkConf().setAppName(appName).setMaster(masterUri)
//				.set("spark.sql.csv.parser.columnPruning.enabled", "false").set("spark.testing.memory", "2147480000");
//	}
//
//	//@Bean
//	public JavaSparkContext javaSparkContext() {
//		return new JavaSparkContext(sparkConf());
//	}
//
//	//@Bean
//	public SparkSession sparkSession() {
//		SparkSession sparkSession = SparkSession.builder().sparkContext(javaSparkContext().sc()).appName(appName)
//				.getOrCreate();
//		sparkSession.udf().register("memberIdGenerator",
//				((UDF3<Integer, Integer, Long, Long>) (i1, i2, i3) -> Long.parseLong(i1 + "" + i2 + "" + i3)),
//				DataTypes.LongType);
//		sparkSession.udf().register("memberIdGenerator1", (UDF2<Integer, Long, Long>) (i1, i2) -> System.nanoTime(),
//				DataTypes.LongType);
//
//		sparkSession.udf().register("getNanoTime", (UDF0<Long>) System::nanoTime, DataTypes.LongType);
//		return sparkSession;
//	}
//
//}
