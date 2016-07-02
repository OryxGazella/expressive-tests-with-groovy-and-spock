package soy.frank.flutterby;

import soy.frank.flutterby.actors.PhysicalEntity;

class CollisionDetector {
    public static boolean collides(PhysicalEntity rect1, PhysicalEntity rect2) {
        return rect1.position().x() < rect2.position().x() + rect2.width() &&
                rect1.position().x() + rect1.width() > rect2.position().x() &&
                rect1.position().y() < rect2.position().y() + rect2.height() &&
                rect1.height() + rect1.position().y() > rect2.position().y();
    }
}
