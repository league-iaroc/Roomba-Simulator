package org.jointheleague;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

class EndZone implements Displayable {
	private int red = 100;
	private int increment = 2;
	private int x, y, radius;
	private Body body;

	public EndZone(int x, int y, int r) {
		this.x = x;
		this.y = y;
		this.radius = r;

		makeBody(x, y, radius);
		body.setUserData(this);
		body.setAngularVelocity(17.2f);
	}

	@Override
	public void display(Processing g) {
		red += increment;
		g.fill(red, 255, red);
		g.noStroke();
		if (red <= 0 || red >= 255) {
			increment = -increment;
		}
		g.ellipse(x, y, radius * 2, radius * 2);
	}

	private void makeBody(float x, float y, float r) {
		BodyDef bd = new BodyDef();
		bd.position = Processing.WORLD.coordPixelsToWorld(x, y);
		bd.type = BodyType.DYNAMIC;
		body = Processing.WORLD.createBody(bd);
		CircleShape cs = new CircleShape();
		cs.m_radius = Processing.WORLD.scalarPixelsToWorld(r);
		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		fd.density = 0;
		fd.friction = 0.5f;
		fd.restitution = 0.1f;
		body.createFixture(fd);
	}

	public Body getBody() {
		return body;
	}
}
