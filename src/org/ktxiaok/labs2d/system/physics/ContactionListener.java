package org.ktxiaok.labs2d.system.physics;

import org.jbox2d.callbacks.ContactImpulse;

public interface ContactionListener {
	void preSolve(ContactHandler contactHandler);

	void postSolve(ContactHandler contactHandler, ContactImpulse impulse);

	void endContact(ContactHandler contactHandler);

	void beginContact(ContactHandler contactHandler);
}
