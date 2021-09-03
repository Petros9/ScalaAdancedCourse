package lectures.part4impl

object TypeClasses extends App {

  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml: String = s"<div>$name ($age yo) <a href=$email/> </div>"
  }

  User("john", 32, "example@email.com").toHtml


  // option 2 - pattern matching

  object HTMLSerializerPM {
    def serializeToHTML(value: Any) = value match {
      case User(n, a, e) =>
      case java.util.Date =>
      case _ =>
    }
  }

  // lost type safety
  // need to modify the code every time

  // option 3

  trait HTMLSerializer[T]{
    def serialize(value: T): String
  }

  object UserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"
  }

  import java.util.Date
  object DateSerializer extends HTMLSerializer[Date]{
    override def serialize(date: Date): String = s"<div> ${date.toString}</div>"
  }

  trait MyTypeClassTemplate[T] {
    def action(value: T): String
  }

  object MyTypeClassTemplate {
    def apply[T](implicit instance: MyTypeClassTemplate[T]): MyTypeClassTemplate[T] = instance
  }

  trait Equal[T] {
    def apply(a: T, b: T): Boolean

    
  }

  implicit object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }

  object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
  }


  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div> $value </div>"
  }
  implicit object StringSerializer extends HTMLSerializer[String] {
    override def serialize(value: String): String = s"<div> String: $value </div>"
  }
  println(HTMLSerializer.serialize(42))
  println(HTMLSerializer.serialize("something"))

  // access to the entire type class interface

  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.apply(a, b)
  }

  val john = User("John", 32, "a@a.com")
  println(Equal(john, john))




  implicit class HTMLEnrichment[T](value: T) {
    def toHTML(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
  }


  println(2.toHTML)

  implicit class TypeSafeEqual[T](value: T) {
    def ===(other: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(value, other)

    def !==(other: T)(implicit equalizer: Equal[T]): Boolean = ! equalizer.apply(value, other)
  }

  def htmlBoilerplate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
    s"<html> ${content.toHTML(serializer)}</html>"


  def htmlSugar[T: HTMLSerializer](content: T): String = {
    val serializer = implicitly[HTMLSerializer[T]]
    s"<html>${content.toHTML(serializer)}</html>"
  }

  case class Permissions(mask: String)
  implicit val defaultPermissions: Permissions = Permissions("0744")

  // in some other part of the code

  val standardPerms = implicitly[Permissions]
}
