package dzufferey.scadla.examples.fastener

import dzufferey.scadla._
import dzufferey.scadla.utils._
import math._
import InlineOps._

object Nut {

  private lazy val thread = new MetricThread()

  def apply(radius: Double) = {
    thread.hexNutIso(radius*2, 1.6 * radius)
  }

  //metric versions (ISO)
  val M1   = apply( Thread.ISO.M1 )
  val M1_2 = apply( Thread.ISO.M1_2 )
  val M1_6 = apply( Thread.ISO.M1_6 )
  val M2   = apply( Thread.ISO.M2 )
  val M2_5 = apply( Thread.ISO.M2_5 )
  val M3   = apply( Thread.ISO.M3 )
  val M4   = apply( Thread.ISO.M4 )
  val M5   = apply( Thread.ISO.M5 )
  val M6   = apply( Thread.ISO.M6 )
  val M8   = apply( Thread.ISO.M8 )
  val M10  = apply( Thread.ISO.M10 )
  val M12  = apply( Thread.ISO.M12 )
  val M16  = apply( Thread.ISO.M16 )
  val M20  = apply( Thread.ISO.M20 )
  val M24  = apply( Thread.ISO.M24 )
  val M30  = apply( Thread.ISO.M30 )
  val M36  = apply( Thread.ISO.M36 )
  val M42  = apply( Thread.ISO.M42 )
  val M48  = apply( Thread.ISO.M48 )
  val M56  = apply( Thread.ISO.M56 )
  val M64  = apply( Thread.ISO.M64 )

}

/** simple Hexagon as placeholder for nuts (rendering is much faster) */
class NutPlaceHolder(tolerance: Double = 0.1) {

  def apply(radius: Double) = {
    Hexagon(radius * 3.2 + tolerance, 1.6 * radius + tolerance)
  }

  val M1   = apply( Thread.ISO.M1 )
  val M1_2 = apply( Thread.ISO.M1_2 )
  val M1_6 = apply( Thread.ISO.M1_6 )
  val M2   = apply( Thread.ISO.M2 )
  val M2_5 = apply( Thread.ISO.M2_5 )
  val M3   = apply( Thread.ISO.M3 )
  val M4   = apply( Thread.ISO.M4 )
  val M5   = apply( Thread.ISO.M5 )
  val M6   = apply( Thread.ISO.M6 )
  val M8   = apply( Thread.ISO.M8 )
  val M10  = apply( Thread.ISO.M10 )
  val M12  = apply( Thread.ISO.M12 )
  val M16  = apply( Thread.ISO.M16 )
  val M20  = apply( Thread.ISO.M20 )
  val M24  = apply( Thread.ISO.M24 )
  val M30  = apply( Thread.ISO.M30 )
  val M36  = apply( Thread.ISO.M36 )
  val M42  = apply( Thread.ISO.M42 )
  val M48  = apply( Thread.ISO.M48 )
  val M56  = apply( Thread.ISO.M56 )
  val M64  = apply( Thread.ISO.M64 )

}
