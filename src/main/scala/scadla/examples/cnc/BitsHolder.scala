package scadla.examples.cnc

import math._
import scadla._
import utils._
import InlineOps._
import scadla.examples.fastener._

object BitsHolder {

  def apply(cols: Int, rows: Int, shankDiammeter: Double, separation: Double, height: Double) = {
    val rounding = min(height, (separation-shankDiammeter)/2)
    val x = cols * separation
    val y = rows * separation
    val z = height
    val base = roundedCube(x,y,z+rounding, rounding).moveZ(-rounding) * Cube(x, y, z)
    val holes = for (i <- 0 until cols; j <- 0 until rows) yield {
      val mx = separation/2 + i * separation
      val my = separation/2 + j * separation
      Cylinder(shankDiammeter/2, height).move(mx, my, 1)
    }
    base -- holes
  }

}
