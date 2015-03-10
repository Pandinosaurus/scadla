package dzufferey.scadla.utils
  
import dzufferey.scadla._

object Hexagon {

  def maxRadius(minRadius: Double) = minRadius / math.sin(math.Pi/3)

  def minRadius(maxRadius: Double) = maxRadius * math.sin(math.Pi/3)

  def apply(minRadius: Double, height: Double) = {
    import scala.math._
    val rd0 = minRadius/2/sin(Pi/3)
    
    val pts = for (i <- 0 until 6; j <- 0 to 1) yield
      Point(rd0 * cos(i * Pi/3), rd0 * sin(i * Pi/3), height * j)
    def face(a: Int, b: Int, c: Int) = Face(pts(a % 12), pts(b % 12), pts(c % 12))

    val side1 = for (i <- 0 until 6) yield face(  2*i, 2*i+2, 2*i+3)
    val side2 = for (i <- 0 until 6) yield face(2*i+1,   2*i, 2*i+3)
    val bottom = Array(
      face(0, 4, 2),
      face(4, 8, 6),
      face(8, 0, 10),
      face(0, 8, 4)
    )
    val top = Array(
      face(1, 3, 5),
      face(5, 7, 9),
      face(9, 11, 1),
      face(1, 5, 9)
    )
    val faces = side1 ++ side2 ++ bottom ++ top
    Polyhedron(faces)
  }

}