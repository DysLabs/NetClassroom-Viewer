package io.github.dyslabs.scala

//import java.net.{Socket,URL,URLConnection,HttpURLConnection,URLEncoder}
import java.io.{BufferedReader,InputStreamReader,OutputStreamWriter}
import com.gargoylesoftware.htmlunit._
import com.gargoylesoftware.htmlunit.html._
//import scala.util._
import scala.collection._
import scala.collection.mutable._

object NCShell {
   def main(args: Array[String]) {
      val webClient:WebClient = new WebClient()
      val page:HtmlPage = webClient.getPage("https://portal.sehs.net/NetClassroom7/Forms/NCShell.aspx")
      // get form, its fields, and submit button
      val form:HtmlForm = page.getFormByName("Form1")
      val login:HtmlSubmitInput=form.getInputByName("btnLogin")
      val sid:HtmlTextInput = form.getInputByName("sid")
      val pin:HtmlPasswordInput=form.getInputByName("pin")
      val stdin:BufferedReader=new BufferedReader(new InputStreamReader(System.in))
      print("username:")
      sid.setValueAttribute(stdin.readLine)
      print("password:")
      pin.setValueAttribute(stdin.readLine)
      val ncShell:HtmlPage=login.click()
      if (!ncShell.getTitleText.equals("NetClassroom")) {
        println("Failed to login")
        return;
      }
      println("================")
      println
      // variables will be named exactly as they appear in the HTML source code
      // _x will be appended as needed
      /*val Table1:HtmlTable = ncShell.getElementById("Table1").asInstanceOf[HtmlTable]
      val tbody:HtmlTableBody = Table1.getBodies().get(0)
      val tr:HtmlTableRow=tbody.getRows.get(1)
      val td:HtmlTableCell=tr.getCell(0)
      val table:HtmlTable=(toArray(td.getChildElements()))(0).asInstanceOf[HtmlTable]
      val tbody_1=table.getBodies.get(0)
      val tr_1=tbody_1.getRows.get(0)
      val td_1=tr_1.getCell(0)
      val div:HtmlDivision=toArray(td_1.getChildElements)(1).asInstanceOf[HtmlDivision]
      val div_1=toArray(div.getChildElements)(0).asInstanceOf[HtmlDivision]*/
      val div:HtmlDivision=ncShell.getElementById("ncContent_cpAverages_collapsible").asInstanceOf[HtmlDivision]
      val div_1=toArray(div.getChildElements)(0)
      val table:HtmlTable=toArray(div_1.getChildElements)(0).asInstanceOf[HtmlTable]
      val tbody=table.getBodies.get(0)
      val rows:java.util.List[HtmlTableRow]=tbody.getRows
      val classes:HashMap[String,Double]=new HashMap
      val iter:java.util.Iterator[HtmlTableRow]=rows.iterator()
      while (iter.hasNext()) {
        val row:HtmlTableRow=iter.next
        val clazz=toArray(row.getCell(0).getChildElements)(0).getTextContent
        val grade=toArray(row.getCell(1).getChildElements)(0).getTextContent
        classes.put(extractClass(clazz), extractNumber(grade))
      }
      var avg:Double=0-1
      var total:Double=0
      var clas:Int=0
      
      classes.foreach(p => {
        var c=p._1
        val g=p._2
        var gpa:Double=0
        if (c.toLowerCase.startsWith("honors")) {
          c="(h) "+c
          gpa=calculate_gpa_h(g)
        } else if (c.toLowerCase.startsWith("ap")||c.toLowerCase.startsWith("ib")) {
          c="(a) "+c
          gpa=calculate_gpa_a(g)
        } else {
          gpa=calculate_gpa_s(g)
        }
        if (avg<0) {
          avg=gpa
        } else {
          avg=avg+gpa
          avg=avg/2
        }
        total=total+gpa
        clas=clas+1
        println(c+" ("+g+") "+gpa+" GPA")
      })
      println("Total GPA: "+total)
      println("Average GPA 1: "+(total/clas))
      println("Average GPA 2: "+avg)
   }
   
   def calculate_gpa_s(grade:Double):Double = {
                if (grade>100 || (grade<=100 && grade>=97)) {
                    return 4.3;
                } else if (grade<97 && grade>=93) {
                    return 4.0;
                } else if (grade<93 && grade>=90) {
                    return 3.7;
                } else if (grade<90 && grade>=87) {
                    return 3.3;
                } else if (grade<87 && grade>=83) {
                    return 3.0;
                } else if (grade<83 && grade>=80) {
                    return 2.7;
                } else if (grade<80 && grade>=77) {
                    return 2.3;
                } else if (grade<77 && grade>=73) {
                    return 2.0;
                } else if (grade<73 && grade>=70) {
                    return 1.7;
                } else if (grade<70 && grade>=67) {
                    return 1.3;
                } else if (grade<67 && grade>=63) {
                    return 1.0;
                } else if (grade<63 && grade>=60) {
                    return 0.7;
                } else {
                    return 0;
                }
            }
            
            def calculate_gpa_h(grade:Double):Double = {
                if (grade<=100 && grade>=97) {
                    return 4.8;
                } else if (grade<97 && grade>=93) {
                    return 4.5;
                } else if (grade<93 && grade>=90) {
                    return 4.2;
                } else if (grade<90 && grade>=87) {
                    return 3.8;
                } else if (grade<87 && grade>=83) {
                    return 3.5;
                } else if (grade<83 && grade>=80) {
                    return 3.2;
                } else if (grade<80 && grade>=77) {
                    return 2.8;
                } else if (grade<77 && grade>=73) {
                    return 2.5;
                } else if (grade<73 && grade>=70) {
                    return 2.2;
                } else if (grade<70 && grade>=67) {
                    return 1.3;
                } else if (grade<67 && grade>=63) {
                    return 1.0;
                } else if (grade<63 && grade>=60) {
                    return 0.7;
                } else {
                    return 0;
                }
            }
            
            def calculate_gpa_a(grade:Double):Double = {
                if (grade<=100 && grade>=97) {
                    return 5.3;
                } else if (grade<97 && grade>=93) {
                    return 5.0;
                } else if (grade<93 && grade>=90) {
                    return 4.7;
                } else if (grade<90 && grade>=87) {
                    return 4.3;
                } else if (grade<87 && grade>=83) {
                    return 4.0;
                } else if (grade<83 && grade>=80) {
                    return 3.7;
                } else if (grade<80 && grade>=77) {
                    return 3.3;
                } else if (grade<77 && grade>=73) {
                    return 3.0;
                } else if (grade<73 && grade>=70) {
                    return 2.7;
                } else if (grade<70 && grade>=67) {
                    return 1.3;
                } else if (grade<67 && grade>=63) {
                    return 1.0;
                } else if (grade<63 && grade>=60) {
                    return 0.7;
                } else {
                    return 0;
                }
            }
   
   def extractClass(input:String):String = {
     input.replaceAll("([A-Z])(?=[0-9])","").replaceAll("[0-9]","").replaceAll("-","").replaceAll(",","").trim
   }
   
   def extractNumber(input:String) = {
     input.replaceAll("\\(", "").replaceAll("\\)","").replaceAll("[A-Z]", "").replaceAll("\\+", "").replaceAll("-", "").toDouble
   }
   
   def toArray(itera:java.lang.Iterable[DomElement]):Array[DomElement] = {
    var i:Int=0
    var iter:java.util.Iterator[DomElement]=itera.iterator
    val list:scala.collection.mutable.ArrayBuffer[DomElement]=new scala.collection.mutable.ArrayBuffer
    while (iter.hasNext) {
      list.insert(i, iter.next())
      i=i+1
    }
    //println(i)
    val ele:Array[DomElement]=new Array[DomElement](i)
    return toArray(list.iterator,i)
  }
   
   def toArray(iter:Iterator[DomElement],size:Int):Array[DomElement] = {
     val ele:Array[DomElement]=new Array[DomElement](size)
     var i:Int=0
     iter.foreach(f => {
       ele(i)=f
       i+=1
     })
     return ele
   }
}
