(define
	(domain world-of-blocks)
	(:requirements :adl)
	(:predicates
		(on-top ?x ?y)
		(on-floor ?x)
		(clear ?x)
		(in-air ?x)
		(sth-in-air)
	)
		; podnieœ klocek z pod³ogi
	(:action podnies-z-podlogi
		:parameters (?x)
		:precondition
		(and
			(clear ?x)
			(on-floor ?x)
			(not (sth-in-air))
		)
		:effect
		(and
			(in-air ?x)
			(sth-in-air)
			(not (on-floor ?x))
			(not (clear ?x))
		)
	)
		; podnies klocek z klocka
	(:action podnies-z-klocka
		:parameters (?x ?y)
		:precondition
		(and
			(on-top ?x ?y)
			(clear ?x)
			(not (sth-in-air))
		)
		:effect
		(and
			(in-air ?x)
			(clear ?y)
			(sth-in-air)
			(not (on-top ?x ?y))
			(not (clear ?x))
		)
	)
		; poloz klocek na podlodze
	(:action opusc-na-podloge
		:parameters (?x)
		:precondition
			(in-air ?x)
		:effect
		(and
			(not (sth-in-air))
			(not (in-air ?x))
			(on-floor ?x)
			(clear ?x)
		)
	)
		; poloz klocek na klocku
	(:action opusc-na-klocek
		:parameters (?y ?x)
		:precondition
		(and
			(in-air ?x)
			(clear ?y)
		)
		:effect
		(and
			(on-top ?x ?y)
			(not (clear ?y))
			(not (in-air ?x))
			(not (sth-in-air))
			(clear ?x)
		)
	)		
)