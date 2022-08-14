package mapping

import mapping.objects.SimpleObject

class ObjectPool {
    public var pool = emptyArray<SimpleObject>()

    public fun append (obj: SimpleObject) {
        pool += obj
    }
}