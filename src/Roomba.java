import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

// The Nature of Code
// Daniel Shiffman
// http://natureofcode.com

// A circular particle

public class Roomba {
	// We need to keep track of a Body and a radius
	Body body;
	float r;
	float angle = 0;
	int tick = 0;
	int col = 0;
	int light = 50;
	int incRed = -4;
	float sizeConstant;
	App app;

	Roomba(float x, float y, float r_, App app) {
		r = r_;
		this.app = app;
		// This function puts the particle in the Box2d world
		makeBody(x, y, r);
		body.setUserData(this);
		// col = app.color(127);
		sizeConstant = r / 20;
	}

	// This function removes the particle from the box2d world
	void killBody() {
		app.box2d.destroyBody(body);
	}

	// Change color when hit
	void change() {
		col = app.color(130, 0, 0);
	}

	// Is the particle ready for deletion?
	boolean done() {
		// Let's find the screen position of the particle
		Vec2 pos = app.box2d.getBodyPixelCoord(body);
		// Is it off the bottom of the screen?
		if (pos.y > app.height + r * 2) {
			killBody();
			return true;
		}
		return false;
	}
	void driveDirect(float left, float right) {
		// possibly use a ques to hold sleep task
		if (tick == 1) {
			float speed = (left + right) / 100;
			float ang = (left - right) / 200;
			drive(speed, ang);
		}
	}

	float getAngle() {
		float temp = angle;
		angle = body.getAngle() * (180 / app.PI);
		return angle - temp;
	}

	void drive(float speed, float ang) {
		float y = app.cos(body.getAngle()) * speed;
		float x = app.sin(body.getAngle()) * speed;

		body.setLinearVelocity(new Vec2(x, y));
		body.setAngularVelocity(ang);

	}

	int redDot() {
		light += incRed;
		if (light <= 0 || light >= 255)
			incRed = -incRed;
		return light;
	}

	//
	void display() {
		tick++;
		if (tick > 10) {
			body.setLinearVelocity(new Vec2(0, 0));
			body.setAngularVelocity(0);
			tick = 0;
		}

		// We look at each body and get its screen position
		Vec2 pos = app.box2d.getBodyPixelCoord(body);
		// Get its angle of rotation
		float a = body.getAngle();
		app.pushMatrix();
		app.translate(pos.x, pos.y);
		app.rotate(a);
		app.fill(col);
		app.stroke(0);
		app.strokeWeight(2);
		app.ellipse(0, 0, r * 2, r * 2);
		app.fill(100);
		app.arc(0, 0, r * 2, r * 2, 0, 2 * app.PI);
		app.fill(col);
		app.arc(0f, 0f, r * 2.15f, r * 2.15f, app.PI * 1.0f, 2.0f * app.PI);
		app.fill(169, 217, 109);
		app.arc(0f, 0f, r * 1.75f, r * 1.75f, 0f, 2f * app.PI);
		app.fill(255, 255, 184);
		app.arc(0, 0, r, r, 0, 2 * app.PI);
		app.fill(20);
		app.arc(0, 0, r * .74f, r * .75f, 0, 2 * app.PI);
		app.fill(230, 242, 244);
		app.arc(0, 0, r / 2, r / 2, 0, 2 * app.PI);
		app.fill(100);
		app.arc(0, 0 + r * .875f, r / 3, r / 3, 0, 2 * app.PI);
		app.fill(100);
		app.arc(0, 0 + r * .875f, r / 4, r / 4, 0, 2 * app.PI);
		app.fill(100);
		app.arc(0, 0 - r, r / 4f, r / 4, 0, 2 * app.PI);
		app.fill(255, redDot(), light);
		app.noStroke();
		app.ellipse(1, 1, 3, 3);
		app.popMatrix();
	}

	// Here's our function that adds the particle to the Box2D world
	void makeBody(float x, float y, float r) {
		// Define a body
		BodyDef bd = new BodyDef();
		// Set its position
		bd.position = app.box2d.coordPixelsToWorld(x, y);
		bd.type = BodyType.DYNAMIC;
		body = app.box2d.createBody(bd);
		// Make the body's shape a circle
		CircleShape cs = new CircleShape();
		cs.m_radius = app.box2d.scalarPixelsToWorld(r);
		FixtureDef fd = new FixtureDef();
		fd.shape = cs;
		// Parameters that affect physics
		fd.density = 1;
		fd.friction = 0.5f;
		fd.restitution = 0.1f;
		// Attach fixture to body
		body.createFixture(fd);
		// body.setAngularVelocity(0);
	}

}