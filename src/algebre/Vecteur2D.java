package algebre;

public class Vecteur2D extends Vecteur {
    public Vecteur2D(double x, double y) {
        super(2);
        coords[0] = x;
        coords[1] = y;
    }

    // sinus de l'angle du vecteur i (axe des abscisses) à this
    public double sinus() {
        double magnitude = Math.sqrt(coords[0] * coords[0] + coords[1] * coords[1]);
        if (magnitude == 0) {
            throw new ArithmeticException("Cannot compute sine for a zero vector.");
        }
        return coords[1] / magnitude;
    }

    public double sinus(Vecteur2D v) {
        double det = this.det(v);

        double magnitudeThis = Math.sqrt(this.coords[0] * this.coords[0] + this.coords[1] * this.coords[1]);
        double magnitudeV = Math.sqrt(v.coords[0] * v.coords[0] + v.coords[1] * v.coords[1]);

        if (magnitudeThis == 0 || magnitudeV == 0) {
            throw new ArithmeticException("Cannot compute sine with a zero vector.");
        }

        return det / (magnitudeThis * magnitudeV);
    }

    private double det(Vecteur2D v) {
        return (this.coords[0] * v.coords[1]) - (coords[1] * v.coords[0]);
    }

    // cosinus de l'angle de i à this
    public double cosinus() {
        double magnitude = Math.sqrt(coords[0] * coords[0] + coords[1] * coords[1]);
        if (magnitude == 0) {
            throw new ArithmeticException("Cannot compute cosine for a zero vector.");
        }
        return coords[0] / magnitude;
    }

    public boolean isVertical() {
        return Math.abs(this.cosinus()) < 1e-10;
    }
    // tangente de l'angle de i à this
    // attention this ne doit pas être vertical...

    public double tangente() {
        if (!this.isVertical()) {
            return coords[1] / coords[0];
        }
        else throw new ArithmeticException("Cannot compute tan for a vertical vector.");
    }

    public double angle() {
        return Math.atan2(coords[1], coords[0]);
    }

    public void setCoords(double x, double y) {
        this.coords[0] = x;
        this.coords[1] = y;
    }

    public double angle(Vecteur2D v) {
        double dotProduct = this.coords[0] * v.coords[0] + this.coords[1] * v.coords[1];
        double magnitudeThis = Math.sqrt(this.coords[0] * this.coords[0] + this.coords[1] * this.coords[1]);
        double magnitudeV = Math.sqrt(v.coords[0] * v.coords[0] + v.coords[1] * v.coords[1]);

        if (magnitudeThis == 0 || magnitudeV == 0) {
            throw new ArithmeticException("Cannot compute angle with a zero vector.");
        }
        double cosTheta = dotProduct / (magnitudeThis * magnitudeV);
        cosTheta = Math.max(-1, Math.min(1, cosTheta));
        return Math.acos(cosTheta);
    }
}
