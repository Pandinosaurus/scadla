package scadla.examples.cnc

import scadla._
import scadla.InlineOps._
import scadla.utils._
import scala.math._

class Frame(var mainBeamLength: Double = 250,
            var clearance: Double = 20,
            var topAngle: Double = Pi/4,
            var topLength: Double = 100,
            var connectorThickness: Double = 3,
            var connectorKnobs: Double = 5,
            var connectorTolerance: Double = Common.tightTolerance) {

  val ew = Extrusion.width
  val ew2 = ew / 2

  def beam(length: Double) = Extrusion(length).moveZ(clearance)
  //protected def beam(length: Double) = Extrusion.placeHolder(length).moveZ(clearance)

  protected def totalLength(beamLength: Double, clearance: Double) =
    beamLength + 2 * clearance

  protected def putAtCorners(s: Solid, radius: Double) = {
    val s2 = for (i <- 0 until 6) yield s.moveX(radius).rotateZ(i * Pi/3)
    Union(s2:_*)
  }

  protected def beamHexaH(beamLength: Double, clearance: Double) = {
    val b = beam(beamLength).rotateX(-Pi/2) //horizontal toward Y axis
    val beamAtCorner = b.rotateZ(Pi/6)
    putAtCorners(beamAtCorner, totalLength(beamLength, clearance))
  }

  protected def beamHexaV(beamLength: Double, clearance: Double) = {
    putAtCorners(beam(beamLength), totalLength(beamLength, clearance))
  }
  
  protected def beamHexaC(beamLength: Double, clearance: Double) = {
    val b = beam(beamLength).rotateY(-Pi/2) //horizontal toward X axis
    putAtCorners(b, 0)
  }

  protected def beamHexaTop(tiltAngle: Double,
                            beamLength: Double,
                            clearance: Double,
                            mainBeamLength: Double) = {
    val b = beam(beamLength).rotateY(-tiltAngle) //tilting toward center
    putAtCorners(b, totalLength(mainBeamLength, clearance))
  }

  private def cl = clearance
  def tl = totalLength( mainBeamLength, clearance)
  def topHeight = tl + cos(topAngle) * topLength

  def skeleton = {
    val topVBeamLength = topLength - 2 * cl
    val topHBeamLength = tl - topLength * sin(topAngle) - 2 * cl
    Union(
      beamHexaH(mainBeamLength, cl),
      beamHexaH(mainBeamLength, cl).moveZ(tl),
      beamHexaV(mainBeamLength, cl),
      beamHexaC(mainBeamLength, cl),
      beamHexaTop(topAngle, topVBeamLength, cl, mainBeamLength).moveZ(tl),
      beamHexaH(topHBeamLength, cl).moveZ(topHeight),
      beamHexaC(topHBeamLength, cl).moveZ(topHeight) //TODO can these beams be replaced by wires ?
    )
  }
  
  //TODO the different types of connector
  //TODO the diagonal things (rigidity) → some form of static rope

  protected def c0 = Extrusion.connector(connectorThickness, connectorKnobs, connectorTolerance)

  protected def connectorCorner = {
    val α = atan2(ew2, cl) - 1e-6
    val β = Pi/3 - 2 * α
    val inner = (cl - connectorThickness) / cos(α)
    val outer = cl / cos(α)
    val p = PieSlice(outer, inner, β, ew)
    p.rotateZ(α).moveZ(-ew2)
  }

  protected def connectorCornerHalf = {
    val α = atan2(ew2, cl) - 1e-6
    val β = Pi/3 - 2 * α
    val inner = (cl - connectorThickness) / cos(α)
    val outer = cl / cos(α)
    val p = PieSlice(outer, inner, β/2, ew)
    (p.rotateZ(α).moveZ(-ew2),
     p.rotateZ(α + β/2 - 5e-7).moveZ(-ew2))
  }
  
  //the corners' corners
  protected def connectorCornerTriple = {
    val (ch1, ch2) = connectorCornerHalf
    val c0 = ch1.moveZ(ew2)
    val c1 = ch2.moveZ(ew2)
    val c2 = connectorCorner.moveZ(-ew2).rotateX(Pi/2)
    val c3 = connectorCorner.moveZ(ew2).rotate(Pi/2, 0, Pi/3)
    Union(c0 * c2, c1 * c3)
  }
  protected def connectorCornerDouble = {
    val c0 = connectorCorner.moveZ(ew2)
    val c1 = connectorCorner.moveZ(-ew2).rotateX(Pi/2)
    val c2 = connectorCorner.moveY(ew2 * (sqrt(2) - 1))rotateX(Pi/4)
    Intersection(c0, c1, c2)
  }

  def connectorBottom = {
    val corners = Seq(
      connectorCorner,                             //bottom left
      connectorCorner.rotateX(Pi/2),               //middle center
      connectorCorner.rotateX(Pi),                 //bottom right
      connectorCorner.rotate(Pi/2,     0,  Pi/3),  //middle left
      connectorCorner.rotate(Pi/2,     0, -Pi/3),  //middle right
      connectorCorner.rotate(   0, -Pi/2, Pi),     //top right
      connectorCorner.rotate(Pi/2, -Pi/2, Pi),     //top center
      connectorCorner.rotate(  Pi, -Pi/2, Pi)      //top left
    )

    val connector = c0.moveZ(cl)
    val connectors = Union(
      connector,
      connector.rotate(0, Pi/2,     0),
      connector.rotate(0, Pi/2,  Pi/3),
      connector.rotate(0, Pi/2, -Pi/3)
    )

    val b0 = Cube(connectorThickness, ew, connectorThickness).move(ew2, -ew2, -connectorThickness)
    val b1 = b0.moveZ(cl).rotateY(Pi/2)
    val base = Hull(
      b0.move(-ew, 0, -ew2),
      b1,
      b1.rotateZ( Pi/3),
      b1.rotateZ(-Pi/3)
    )

    val pSize = 5.0
    val p0 = {
      val c = Cube(pSize, pSize, cl+ew2)
      Intersection(c, c.rotateZ(Pi/4)).move(-pSize/2, -pSize/2, -ew2)
    }
    val pillars = Union(
      p0.move(-ew2+pSize/2, -ew2+pSize/2, 0),
      p0.move(-ew2+pSize/2,  ew2-pSize/2, 0)
    )

    Union(
      base,
      pillars,
      connectors,
      corners(0),
      corners(2),
      //TODO better than Hull
      Hull(corners(1), corners(6)),
      Hull(corners(4),
           corners(5),
           connectorCornerTriple.rotateZ(-Pi/3),
           connectorCornerDouble.rotate(0,-Pi/2,-Pi)
      ),
      Hull(corners(3),
           corners(7),
           connectorCornerTriple,
           connectorCornerDouble.rotate(0,-Pi/2,-Pi/2)
      )
      //TODO to attach cables
    )
  }
  
  def connectorMiddle = {
    ???
  }

  def connectorTop = {
    ???
  }

  def connectorCenter = {
    val c1 = c0.rotateY( Pi/2)
    putAtCorners(c1, cl) + putAtCorners(connectorCorner, 0)
  }

  def connections = {
    Union(
      connectorCenter,
      connectorCenter.moveZ(topHeight)
    )
  }

  def full = skeleton + connections


}

object Frame {

  def apply(mainBeamLength: Double,
            clearance: Double,
            topAngle: Double,
            topLength: Double) = {
    val f = new Frame(mainBeamLength, clearance, topAngle, topLength)
    //f.connections
    //f.full
    //f.connectorBottom
    f.connectorMiddle
  }

}
