package level1;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

import shiffman.box2d.Box2DProcessing;

public class Wall {
	float x;
	float y;
	float width;
	float height;
	boolean bound;
	Body b;
	private Box2DProcessing box2d;

	Wall(float x_, float y_, float w_, float h_, boolean bound_, Box2DProcessing box2d) {
		this.box2d = box2d;
		x = x_;
		y = y_;
		width = w_;
		height = h_;
		bound = bound_;
		PolygonShape sd = new PolygonShape();
		float box2dW = box2d.scalarPixelsToWorld(width / 2);
		float box2dH = box2d.scalarPixelsToWorld(height / 2);
		sd.setAsBox(box2dW, box2dH);
		BodyDef bd = new BodyDef();
		bd.type = BodyType.STATIC;
		bd.position.set(box2d.coordPixelsToWorld(x, y));
		b = box2d.createBody(bd);
		b.createFixture(sd, 1);
		b.setUserData(this);
	}

	void display(Processing g) {
		g.fill(255);
		g.stroke(0);
		g.rectMode(g.CENTER);
		g.rect(x, y, width, height);
	}

	void killBody() {
		box2d.destroyBody(b);
	}

	boolean done(Processing g) {
		Vec2 pos = box2d.getBodyPixelCoord(b);
		if (!bound && pos.y >= g.mouseY - 20 && pos.x >= g.mouseX - 20 && pos.y <= g.mouseY + 20
				&& pos.x <= g.mouseX + 20) {
			killBody();
			return true;
		}
		return false;
	}
}