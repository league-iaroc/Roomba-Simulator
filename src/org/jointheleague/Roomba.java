package org.jointheleague;

import static processing.core.PConstants.PI;

import java.io.Serializable;
import java.util.UUID;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

class Roomba implements Displayable, Serializable {
	private static final long serialVersionUID = -3922395008408630269L;
	private String id;
	private int tick = 0;
	private int light = 50;
	private int incRed = -4;
	private float x, y;
	private float radius;
	private boolean bump;
	private transient Body body;

	public Roomba(float x, float y, float radius) {
		this.radius = radius;
		this.x = x;
		this.y = y;
		this.id = UUID.randomUUID().toString();

		makeBody();
	}

	public void killBody() {
		Processing.WORLD.destroyBody(body);
	}

	public void driveDirect(float left, float right) {
		if (tick == 1) {
			float speed = (left + right) / (Processing.SCREEN_SIZE / (Processing.GRID_SIZE * 2.0f));
			float ang = (left - right) / (Processing.SCREEN_SIZE / (float) (Processing.GRID_SIZE));
			drive(speed, ang);
		}
	}

	private void drive(float speed, float angle) {
		float y = (float) (Math.cos(body.getAngle()) * speed);
		float x = (float) (Math.sin(body.getAngle()) * speed);

		body.setLinearVelocity(new Vec2(x, y));
		body.setAngularVelocity(angle);
	}

	private int drawRedDot() {
		light += incRed;
		if (light <= 0 || light >= 255)
			incRed = -incRed;
		return light;
	}

	@Override
	public void display(Processing g) {
		tick++;
		if (tick > 10) {
			body.setLinearVelocity(new Vec2(0, 0));
			body.setAngularVelocity(0);
			tick = 0;
		}

		Vec2 pos = Processing.WORLD.getBodyPixelCoord(body);
		float a = body.getAngle();
		g.pushMatrix();
		g.translate(pos.x, pos.y);
		g.rotate(a);
		g.fill(0);
		g.stroke(0);
		g.strokeWeight(2);
		g.ellipse(0, 0, radius * 2, radius * 2);
		g.fill(100);
		g.arc(0, 0, radius * 2, radius * 2, 0, 2 * PI);
		g.fill(0);
		g.arc(0f, 0f, radius * 2.15f, radius * 2.15f, PI * 1.0f, 2.0f * PI);
		g.fill(169, 217, 109);
		g.arc(0f, 0f, radius * 1.75f, radius * 1.75f, 0f, 2f * PI);
		g.fill(255, 255, 184);
		g.arc(0, 0, radius, radius, 0, 2 * PI);
		g.fill(20);
		g.arc(0, 0, radius * .74f, radius * .75f, 0, 2 * PI);
		g.fill(230, 242, 244);
		g.arc(0, 0, radius / 2, radius / 2, 0, 2 * PI);
		g.fill(100);
		g.arc(0, 0 + radius * .875f, radius / 3, radius / 3, 0, 2 * PI);
		g.fill(100);
		g.arc(0, 0 + radius * .875f, radius / 4, radius / 4, 0, 2 * PI);
		g.fill(100);
		g.arc(0, 0 - radius, radius / 4f, radius / 4, 0, 2 * PI);
		g.fill(255, drawRedDot(), light);
		g.noStroke();
		g.ellipse(1, 1, 3, 3);
		g.popMatrix();
	}

	public void makeBody() {
		BodyDef bd = new BodyDef();
		bd.position = Processing.WORLD.coordPixelsToWorld(x, y);
		bd.type = BodyType.DYNAMIC;
		body = Processing.WORLD.createBody(bd);

		CircleShape cs = new CircleShape();
		cs.m_radius = Processing.WORLD.scalarPixelsToWorld(radius);

		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		fd.density = 0;
		fd.friction = 0.5f;
		fd.restitution = 0.1f;

		body.createFixture(fd);
		body.setUserData(this);
		body.setAngularVelocity(17.2f);
	}

	public boolean isBump() {
		return bump;
	}

	public void setBump(boolean bump) {
		this.bump = bump;
	}

	public String getID() {
		return id;
	}

	public Body getBody() {
		return body;
	}
}