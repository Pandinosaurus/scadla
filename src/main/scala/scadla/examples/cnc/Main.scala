package scadla.examples.cnc

import scadla._
import scadla.InlineOps._
import scala.math._

object Main{
    
  val r = backends.OpenSCAD
  //val r = backends.JCSG
  //val r = new backends.ParallelRenderer(backends.OpenSCAD)
  //val r = new backends.ParallelRenderer(backends.JCSG)
  
  def main(args: Array[String]) {
  //r.toSTL(BitsHolder(4, 2, 3.5, 16, 12), "bits_holder.stl")

  //Spindle.objects.par.foreach{ case (name, obj) => 
  //  r.toSTL(obj, name + ".stl")
  //}
  //r.toSTL(Spindle.collet, "collet.stl")
  //r.toSTL(Spindle.spindle, "spindle.stl")
  //r.toSTL(Spindle.colletWrench, "wrench.stl")

  //new Joint2DOF().parts.zipWithIndex.par.foreach{ case (p, i) =>
  //  r.toSTL(p, "j2dof_" + i + ".stl")
  //}

  //r.toSTL(Platform(4, 10, 10, 1.6), "platform.stl")

    r.view(Nema17(36))

  //r.view(Frame(250, 20, Pi/4, 100))
  //r.toSTL(Frame(250, 20, Pi/4, 100), "frame.stl")
  //r.view(Extrusion.connector(3, 5))

    println("work in progress")
  }

}