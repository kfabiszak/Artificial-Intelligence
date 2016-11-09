(define (problem p1)
	(:domain world-of-blocks)
	(:objects a b c d e f g h)
	(:init
		(clear a) 
		(clear b) 
		(clear c) 
		(clear d) 
		(clear e) 
		(clear f) 
		(clear g) 
		(clear h)
		(on-floor a)
		(on-floor b)
		(on-floor c)
		(on-floor d)
		(on-floor e)
		(on-floor f)
		(on-floor g)
		(on-floor h)
	)
	(:goal
		(and
			; zadanie 1
			(on-top d b)
			(or (on-top b c) (on-top b a) (on-top b e))
			
			; zadanie 2
			;(on-floor a)
			;(on-floor b)
			;(on-floor c)
			;(on-floor d)
			;(on-floor e)
			
			; zadanie 3
			;(or (on-floor a) (on-floor b) (on-floor c) (on-floor d) (on-floor e) (on-floor f) (on-floor g) (on-floor h))
			;(or (clear a) (clear b) (clear c) (clear d) (clear e) (clear f) (clear g) (clear h))
		)
	)
)