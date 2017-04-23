package org.jointheleague;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

import shiffman.box2d.Box2DProcessing;

public class Wall implements Displayable {
	private float x, y, width, height;
	private Body b;
	private Box2DProcessing box2d;

	public Wall(float x_, float y_, float w_, float h_, Box2DProcessing box2d) {
		this.box2d = box2d;
		this.x = x_;
		this.y = y_;
		this.width = w_;
		this.height = h_;
		PolygonShape sd = new PolygonShape();
		float box2dW = box2d.scalarPixelsToWorld(width / 2);
		float box2dH = box2d.scalarPixelsToWorld(height / 2);
		sd.setAsBox(box2dW, box2dH);
		BodyDef bd = new BodyDef();
		bd.type = BodyType.STATIC;
		bd.position.set(box2d.coordPixelsToWorld(x, y));
		this.b = box2d.createBody(bd);
		this.b.createFixture(sd, 1);
		this.b.setUserData(this);
	}

	@Override
	public void display(Processing g) {
		g.fill(255);
		g.stroke(0);
		g.rectMode(Processing.CENTER);
		g.rect(x, y, width, height);
	}

	public void killBody() {
		box2d.destroyBody(b);
	}
}