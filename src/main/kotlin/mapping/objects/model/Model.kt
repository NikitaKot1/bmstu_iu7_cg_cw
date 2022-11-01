package mapping.objects.model

import mapping.objects.model.parts.Dot
import mapping.objects.model.parts.Facet

class Model (){
    public var center = Dot(.0, .0, .0)
    public var facets = mutableListOf<Facet>()
    public var norms = mutableListOf<Facet>()
}