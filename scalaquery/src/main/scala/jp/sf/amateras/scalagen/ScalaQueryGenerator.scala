package jp.sf.amateras.scalagen

import jp.sf.amateras.scalagen._

/**
 * A generator implementation for ScalaQuery.
 */
class ScalaQueryGenerator extends GeneratorBase {

  def generate(settings: Settings, table: Table): String = {
    import settings._

    (if(packageName == ""){
      ""
    } else {
      "package " + packageName + "\n\n"
    }) +
    "import org.scalaquery.ql.basic.{BasicTable => Table}\n\n" +
    "object " + table.className + "Table extends Table[" + table.className + "](\"" + table.name + "\"){\n" +
    table.columns.map { column =>
      "  def " + column.propertyName + " = column[" + propertyType(column) + "](\"" + column.name + "\")"
    }.mkString("\n") +
    "\n" +
    "  def * = " + table.columns.map { _.propertyName }.mkString(" ~ ") + " <> (" + table.className + ", " + table.className + ".unapply _)\n" +
    "}\n\n" +
    "case class " + table.className + "(\n" +
    table.columns.map { column => "  " + column.propertyName + ": " + propertyType(column) }.mkString(",\n") +
    "\n" +
    ")\n"
  }

  private def propertyType(column: Column): String =
    if(column.nullable) { "Option[" + column.dataType + "]" } else { column.dataType }


}

