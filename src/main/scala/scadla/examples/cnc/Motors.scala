package scadla.examples.cnc

import scadla._
import scadla.InlineOps._
import scadla.utils._
import scala.math._

//place holder for NEMA stepper motors

object Nema14 {
  
  def apply( length: Double,
             screwLength: Double,
             axisFlat: Double,
             axisLengthFront: Double,
             axisLengthBack: Double ): Solid = {
    NemaStepper(35.2, length,
                26, Thread.ISO.M3, screwLength,
                11, 2.0,
                2.5, axisFlat, axisLengthFront, axisLengthBack)
  }
  
  def apply( length: Double,
             screwLength: Double,
             axisFlat: Double ): Solid = {
    apply(length, screwLength, axisFlat, 22, 0)
  }

  def apply( length: Double): Solid = {
    apply(length, 3, 0.45, 22, 0)
  }

}

object Nema17 {

  def apply( length: Double,
             screwLength: Double,
             axisFlat: Double,
             axisLengthFront: Double,
             axisLengthBack: Double ): Solid = {
    NemaStepper(42.3, length,
                31, Thread.ISO.M3, screwLength,
                11, 2.0,
                2.5, axisFlat, axisLengthFront, axisLengthBack)
  }
  
  def apply( length: Double,
             screwLength: Double,
             axisFlat: Double ): Solid = {
    apply(length, screwLength, axisFlat, 25, 0)
  }

  def apply( length: Double): Solid = {
    apply(length, 5, 0.45, 22, 0)
  }

}

object NemaStepper {

  def apply( side: Double,
             length: Double,
             screwSeparation: Double,
             screwSize: Double,
             screwLength: Double,
             flangeRadius: Double,
             flangeDepth: Double,
             axisRadius: Double,
             axisFlat: Double,
             axisLengthFront: Double,
             axisLengthBack: Double ) = {
    val base = centeredCubeXY(side, side, length).moveZ(-length)
    val withFlange = if (flangeDepth > 0) base + Cylinder(flangeRadius, flangeDepth) else base
    val screw = Cylinder(screwSize, screwLength.abs)
    val screws = Union(
      screw.move( -screwSeparation/2, -screwSeparation/2, 0),
      screw.move( -screwSeparation/2,  screwSeparation/2, 0),
      screw.move(  screwSeparation/2, -screwSeparation/2, 0),
      screw.move(  screwSeparation/2,  screwSeparation/2, 0)
    )
    val withScrews =
      if (screwLength > 0) {
        withFlange - screws.moveZ(-screwLength)
      } else if (screwLength < 0) {
        withFlange + screws
      } else {
        withFlange
      }
    def axis(length: Double) = {
      val a = Cylinder(axisRadius, length)
      if (axisFlat > 0) {
        a - Cube(2*axisRadius, 2*axisRadius, length).move(axisRadius - axisFlat, -axisRadius, 0)
      } else {
        a
      }
    }
    val withFrontAxis = withScrews + axis(axisLengthFront)
    val withBackAxis = withFrontAxis + axis(axisLengthBack).moveZ(-length -axisLengthBack)
    withBackAxis
  }

}