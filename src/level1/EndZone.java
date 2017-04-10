package level1;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import shiffman.box2d.Box2DProcessing;

public class EndZone  {
	private int red = 100;
	private int increment = 2;
	int x;
	int y;
	int radius;
	Body body;
	Box2DProcessing box2d;
	public EndZone(int x, int y, int r, Box2DProcessing box2d) {
		this.x = x;
		this.y = y;
		this.radius = r;
		this.box2d = box2d;
		makeBody(x, y, radius);
		body.setUserData(this);
		body.setAngularVelocity(17.2f);
	}

	void display(Processing g) {
		red += increment;
		g.fill(red, 255, red);
		g.noStroke();
		if (red <= 0 || red >= 255) {
			increment = -increment;
		}
		g.ellipse(x, y, radius * 2, radius * 2);
	}
	void makeBody(float x, float y, float r) {
		BodyDef bd = new BodyDef();
		bd.position = box2d.coordPixelsToWorld(x, y);
		bd.type = BodyType.DYNAMIC;
		body = box2d.createBody(bd);
		CircleShape cs = new CircleShape();
		cs.m_radius = box2d.scalarPixelsToWorld(r);
		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		fd.density = 0;
		fd.friction = 0.5f;
		fd.restitution = 0.1f;
		body.createFixture(fd);
	}
	Body getBody(){
		return body;
	}
}
