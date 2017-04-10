package level1;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import processing.core.PConstants;
import shiffman.box2d.Box2DProcessing;

public class Roomba {
	Body body;
	float radius;
	float angle = 0;
	int tick = 0;
	int light = 50;
	int incRed = -4;
	float x;
	float y;
	float r;
	Box2DProcessing box2d;

	Roomba(float x, float y, float radius, Box2DProcessing box2d) {
		this.radius = radius;
		this.box2d = box2d;
		makeBody(x, y, radius);
		body.setUserData(this);
		body.setAngularVelocity(17.2f);
	}

	void killBody() {
		box2d.destroyBody(body);
	}

	boolean done() {
		Vec2 pos = box2d.getBodyPixelCoord(body);
		if (pos.y > Processing.SCREEN_SIZE + radius * 2) {
			killBody();
			return true;
		}
		return false;
	}

	void driveDirect(float left, float right) {
		if (tick == 1) {
			float speed = (left + right) / (Processing.SCREEN_SIZE/(Processing.GRID_SIZE*2.0f));
			float ang = (left - right) / (Processing.SCREEN_SIZE/(float)(Processing.GRID_SIZE));
			drive(speed, ang);
		}
	}

	float getAngle() {
		float temp = angle;
		angle = (float) (body.getAngle() * (180 / Math.PI));
		return angle - temp;
	}

	void drive(float speed, float angle) {
		float y = (float) (Math.cos(body.getAngle()) * speed);
		float x = (float) (Math.sin(body.getAngle()) * speed);

		body.setLinearVelocity(new Vec2(x, y));
		body.setAngularVelocity(angle);

	}

	int drawRedDot() {
		light += incRed;
		if (light <= 0 || light >= 255)
			incRed = -incRed;
		return light;
	}
	


	//
	void display(Processing g) {
		tick++;
		if (tick > 10) {
			body.setLinearVelocity(new Vec2(0, 0));
			body.setAngularVelocity(0);
			tick = 0;
		}

		// We look at each body and get its screen position
		Vec2 pos = g.box2d.getBodyPixelCoord(body);
		// Get its angle of rotation
		float a = body.getAngle();
		g.pushMatrix();
		g.translate(pos.x, pos.y);
		g.rotate(a);
		g.fill(0);
		g.stroke(0);
		g.strokeWeight(2);
		g.ellipse(0, 0, radius * 2, radius * 2);
		g.fill(100);
		g.arc(0, 0, radius * 2, radius * 2, 0, 2 * g.PI);
		g.fill(0);
		g.arc(0f, 0f, radius * 2.15f, radius * 2.15f, g.PI * 1.0f, 2.0f * g.PI);
		g.fill(169, 217, 109);
		g.arc(0f, 0f, radius * 1.75f, radius * 1.75f, 0f, 2f * g.PI);
		g.fill(255, 255, 184);
		g.arc(0, 0, radius, radius, 0, 2 * g.PI);
		g.fill(20);
		g.arc(0, 0, radius * .74f, radius * .75f, 0, 2 * g.PI);
		g.fill(230, 242, 244);
		g.arc(0, 0, radius / 2, radius / 2, 0, 2 * g.PI);
		g.fill(100);
		g.arc(0, 0 + radius * .875f, radius / 3, radius / 3, 0, 2 * g.PI);
		g.fill(100);
		g.arc(0, 0 + radius * .875f, radius / 4, radius / 4, 0, 2 * g.PI);
		g.fill(100);
		g.arc(0, 0 - radius, radius / 4f, radius / 4, 0, 2 * g.PI);
		g.fill(255, drawRedDot(), light);
		g.noStroke();
		g.ellipse(1, 1, 3, 3);
		g.popMatrix();
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

}