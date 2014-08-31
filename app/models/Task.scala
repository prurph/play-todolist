package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Task(id: Long, label: String)

object Task {

//  task is a parser that takes a JDBC ResultSet row with at least an id and label
//  can create a Task value
  val task = {
    get[Long]("id") ~
    get[String]("label") map {
      case id~label => Task(id, label)
    }
  }

//  the Play DB.withConnection helper creates/automatically releases a JDBC conn
  def all(): List[Task] = DB.withConnection { implicit c =>
    SQL("SELECT * FROM task").as(task *)
  }
  def create(label: String) = DB.withConnection { implicit c =>
    SQL("INSERT INTO task (label) VALUES ({label})").on(
      'label -> label
    ).executeUpdate()
  }
  def delete(id: Long) = DB.withConnection { implicit c =>
    SQL("DELETE FROM task WHERE id = {id}").on(
      'id -> id
    ).executeUpdate()
  }
}
