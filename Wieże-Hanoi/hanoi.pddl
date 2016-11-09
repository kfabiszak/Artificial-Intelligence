(define 
	(domain hanoi)
	(:requirements :adl)
	(:predicates
		(on-top ?k1 ?k2)
		(on ?d ?k)
		(clear ?k)
		(empty ?d)
		(bigger ?k1 ?k2)
	)
		; przesuwa na pusty dr¹zek
	(:action przesun-na-pusty
		:parameters (?d1 ?d2 ?k)
		:precondition
		(and
			(not (= ?d1 ?d2))
			(clear ?k)
			(on ?d1 ?k)
			(empty ?d2)
		)
		:effect
		(and
			(on ?d2 ?k)
			(not (on ?d1 ?k))
			(not (empty ?d2))
			; czy drazek zostal pusty
			; co z krazkiem pod zdjetym krazkiem
		)	
	)
		; przesuwa krazek na inny krazek
	(:action przesun
		:parameters (?d1 ?d2 ?k1 ?k2)
		:precondition
		(and
			(not (= ?d1 ?d2))
			(not (= ?k1 ?k2))
			(clear ?k1)
			(clear ?k2)
			(bigger ?k2 ?k1)
			(not (empty ?d2))
			(on ?d1 ?k1)
			(on ?d2 ?k2)
			
		)
		:effect
		(and
			(not (clear ?k2))
			(on-top ?k1 ?k2)
			(not (on ?d1 ?k1))
			(on ?d2 ?k1)
		)
	)
)