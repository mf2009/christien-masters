package com.pypmannetjies.pointerlocation;

public class Vector2D {

	private double x,y;
	
	public Vector2D(double x, double y) {
		this.setX(x);
		this.setY(y);
	}
	
	public double dotProduct(Vector2D rhs) {
		double x = this.getX() * rhs.getX();
		double y = this.getY() * rhs.getY();
		return x + y;
	}
	
	public Vector2D getNormalized() {
		double length = getLength();
		Vector2D normalized = new Vector2D(this.x / length, this.y / length);
		return normalized;
	}
	
	public void normalize() {
		double length = getLength();
		this.x = this.x / length;
		this.y = this.y / length;
	}
	
	public double getLength() {
		return Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2));
	}
	
	public double getAngle(Vector2D rhs) {
		Vector2D normalised = getNormalized();
		Vector2D rhsNormalised = rhs.getNormalized();
		return Math.acos(normalised.dotProduct(rhsNormalised)); //http://www.euclideanspace.com/maths/algebra/vectors/angleBetween/index.htm
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}
	
}
