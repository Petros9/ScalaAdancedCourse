package lectures.part4impl

import java.util.Date

class JSONSerialization extends App {

  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createAt: Date)
  case class Feed(user: User, posts: List[Post])

  sealed trait JSONValue {
    def stringify: String
  }

  final case class JSONString(value: String) extends JSONValue {
    override def stringify: String = "\"" + value + "\""
  }
  final case class JSONNumber(value: Int) extends JSONValue {
    override def stringify: String = value.toString
  }
  final case class JSONArray(value: List[JSONValue]) extends JSONValue {
    override def stringify: String = value.map(_.stringify).mkString("[",",","]")
  }

  final case class JSONObject(values: Map[String, JSONValue]) extends JSONValue {
    override def stringify: String = values.map{
      case(key, value) => "\"" + key + "\":" + value.stringify
    }
      .mkString("{",",","}")

    val data = JSONObject(Map(
      "user" -> JSONString("Mike"),
      "posts" -> JSONArray(List(
        JSONString("Random"),
        JSONNumber(123)
      ))
    ))
  }


  // type class
  trait JSONConverter[T] {
    def convert(value: T): JSONValue
  }

  // type class instances
  implicit object StringConverter extends JSONConverter[String]{
    def convert(value: String): JSONValue = JSONString(value)
  }

  implicit object NumberConverter extends JSONConverter[Int] {
    def convert(value: Int): JSONValue = JSONNumber(value)
  }

  implicit object UserConverter extends JSONConverter[User] {
    def convert(value: User): JSONValue = JSONObject(Map(
      "name" -> JSONString(value.name),
      "age" -> JSONNumber(value.age),
      "email" -> JSONString(value.email)
    ))
  }

  implicit object PostConverter extends JSONConverter[Post]{
    def convert(post: Post): JSONValue = JSONObject(Map(
      "content" -> JSONString(post.content),
      "created:" -> JSONString(post.createAt.toString)
    ))
  }

  implicit object FeedConverter extends JSONConverter[Feed] {
    def convert(feed: Feed): JSONValue = JSONObject(Map(
      "user" -> feed.user.toJSON, // TODO
      "posts" -> JSONArray(feed.posts.map(_.toJSON)) // TODO
    ))
  }

  implicit class JSONOps[T](value: T) {
    def toJSON(implicit converter: JSONConverter[T]): JSONValue =
      converter.convert(value)
  }

  // val now = new Date(System.currentTimeMillis())

}
