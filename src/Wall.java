import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

// The Nature of Code
// Daniel Shiffman
// http://natureofcode.com
// A fixed boundary class

public class Wall {
	// A boundary is a simple rectangle with x,y,width,and height
	float x;
	float y;
	float w;
	float h;
	boolean bound;
	App app;
	// But we also have to make a body for box2d to know about it
	Body b;

	Wall(float x_, float y_, float w_, float h_, boolean bound_, App app) {
		this.app = app;
		x = x_;
		y = y_;
		w = w_;
		h = h_;
		bound = bound_;
		// Define the polygon
		PolygonShape sd = new PolygonShape();
		// Figure out the box2d coordinates
		float box2dW = app.box2d.scalarPixelsToWorld(w / 2);
		float box2dH = app.box2d.scalarPixelsToWorld(h / 2);
		// We're just a box
		sd.setAsBox(box2dW, box2dH);

		// Create the body
		BodyDef bd = new BodyDef();
		bd.type = BodyType.STATIC;
		bd.position.set(app.box2d.coordPixelsToWorld(x, y));
		b = app.box2d.createBody(bd);

		// Attached the shape to the body using a Fixture
		b.createFixture(sd, 1);

		b.setUserData(this);
	}

	// Draw the boundary, if it were at an angle we'd have to do something
	// fancier
	void display() {
		app.fill(255);
		app.stroke(0);
		app.rectMode(app.CENTER);
		app.rect(x, y, w, h);
	}

	void killBody() {
		app.box2d.destroyBody(b);
	}

	boolean done() {
		Vec2 pos = app.box2d.getBodyPixelCoord(b);
		// Is it off the bottom of the screen?
		if (!bound && pos.y >= app.mouseY - 20 && pos.x >= app.mouseX - 20
				&& pos.y <= app.mouseY + 20 && pos.x <= app.mouseX + 20) {
			killBody();
			return true;
		}
		return false;
	}
}