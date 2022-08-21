package mapping.objects

import mapping.objects.Dot

class Model {
    public var center = Dot(.0, .0, .0)
    public var facets = mutableListOf<Facet>()
    public var norms = mutableListOf<Facet>()
}