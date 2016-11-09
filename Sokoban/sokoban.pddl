(define 
	(domain sokoban)
	(:requirements :adl)
	(:predicates
		(pionowo ?x ?y)
		(poziomo ?x ?y)
		(paczka ?x)
		(cel ?x)
		(robot ?x)
	)
		; przesun robota z punktu x do y
	(:action idz
		:parameters (?x ?y)
		:precondition
		(and
			(not (paczka ?y))
			(robot ?x)
			(or
				(poziomo ?x ?y)
				(pionowo ?x ?y)
			)
		)
		:effect
		(and
			(robot ?y)
			(not (robot ?x))
		)
	)
		; przepchnij paczkê z y na z
	(:action pchaj
		:parameters (?x ?y ?z)
		:precondition
		(and
			(not (= ?x ?z))
			(robot ?x)
			(paczka ?y)
			(not (paczka ?z))
			(or	
				(and
					(poziomo ?x ?y)
					(poziomo ?y ?z)
				)
				(and
					(pionowo ?x ?y)
					(pionowo ?y ?z)
				)		
			)
		)
		:effect
		(and
			(robot ?y)
			(not (robot ?x))
			(paczka ?z)
			(not (paczka ?y))
		)
	)
			
)