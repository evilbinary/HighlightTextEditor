;;sys
(define (find-proc name)
    (if (procedure? name)
	name
	(sys:find name (sys:top-env))))
(define (proc-name proc)
    (if (procedure? proc)
	 (sys:lookup-var proc (sys:top-env))
	proc))
(define (update-proc name proc)
    (sys:update name proc (sys:top-env)))

;;add1 sub1
(define (add1 n)
    (+ n 1))
(define (sub1 n)
    (- n 1))

;(define (zero? n)
;(= 0 n))

(define (atom? x) (not (pair? x)))

;(define (map fn l)
;(if (pair? l)
;(cons (fn (car l)) (map fn (cdr l)))
;'()))

;;list helper functions
(define (caar x) (car (car x))) 
(define (cadr x) (car (cdr x))) 
(define (cddr x) (cdr (cdr x))) 
(define (cdar x) (cdr (car x))) 

(define (caaar x) (car (car (car x)))) 
(define (caadr x) (car (car (cdr x)))) 
(define (cadar x) (car (cdr (car x)))) 
(define (caddr x) (car (cdr (cdr x)))) 
(define (cdaar x) (cdr (car (car x)))) 
(define (cdadr x) (cdr (car (cdr x)))) 
(define (cddar x) (cdr (cdr (car x)))) 
(define (cdddr x) (cdr (cdr (cdr x)))) 

(define (caaaar x) (car (car (car (car x))))) 
(define (caaadr x) (car (car (car (cdr x))))) 
(define (caadar x) (car (car (cdr (car x))))) 
(define (caaddr x) (car (car (cdr (cdr x))))) 
(define (cadaar x) (car (cdr (car (car x))))) 
(define (cadadr x) (car (cdr (car (cdr x))))) 
(define (caddar x) (car (cdr (cdr (car x))))) 
(define (cadddr x) (car (cdr (cdr (cdr x))))) 
(define (cdaaar x) (cdr (car (car (car x))))) 
(define (cdaadr x) (cdr (car (car (cdr x))))) 
(define (cdadar x) (cdr (car (cdr (car x))))) 
(define (cdaddr x) (cdr (car (cdr (cdr x))))) 
(define (cddaar x) (cdr (cdr (car (car x))))) 
(define (cddadr x) (cdr (cdr (car (cdr x))))) 
(define (cdddar x) (cdr (cdr (cdr (car x))))) 
(define (cddddr x) (cdr (cdr (cdr (cdr x))))) 


;;empty?
(define (empty? lst) (eq? lst nil)) 

;max min
(define max
  (lambda (x y)
    (if (> x y) x y)))

(define min
  (lambda (x y)
    (if (< x y) x y)))

;;factorial
(define (factorial n)
    (if (= n 1)
	1(* n (factorial (- n 1)))))

;;abs
(define (abs x)
    (cond ((< x 0) (- x))
	  (else x)))

;;<=
(define (<= x y)
    (or (< x y) (= x y)))

;;>=
(define (>= x y)
    (or (> x y) (= x y)))
;;map
(define (map proc items)
  (if (null? items)
      nil
      (cons (proc (car items))
            (map proc (cdr items)))))
;(map abs '( -10 2.5 -11.6 17))
;(map (lambda (n) (* 2 n)) '(1 2 3 4 5 6))
;(map zero? '(1 0 3 0 6))

;nil '()
(define nil '())
;exp
;; Linear recursion
(define (expt b n)
  (if (= n 0)
      1
      (* b (expt b (- n 1)))))


;; Linear iteration
(define (expt b n)
  (expt-iter b n 1))

(define (expt-iter b counter product)
  (if (= counter 0)
      product
      (expt-iter b
                (- counter 1)
                (* b product)))) 
;(expt 2.1 10)

;;;list fun
(define  list
  (lambda values values) ) 

;(define (list . objs) objs)


;(list 'd 'a 1 'ee '(a b c d))
;(list 3 4 'a (car '(b . c)) (+ 6 -2))
;(list (list 1 2) (list 3 4))

;;append ok
(define (append x y) 
    (if (null? x) y
					;(not (pair? x)) (display "first arg must pair")
	(cons (car x) (append (cdr x) y))))

;(trace 'append)
;(trace 'null?)
;(append '(a b c d e) 'b )
;(append 'a 'b)

;;length
(define length
    (lambda (l)
      (if (null? l)
	  0
	  (+ 1 (length (cdr l))))))

;;reverse
(define reverse
    (lambda (l)
      (if (null? l)
	  nil
	  (append (reverse (cdr l)) (list (car l))))))

;(define (reverse l)
;    (define (iter in out)
;	(if (pair? in)
;	    (iter (cdr in) (cons (car in) out))
;	    out))
;  (iter l '()))

;(trace 'null?)
;(trace 'reverse)
;(trace 'list)
;(reverse '())
;(trace 'append)
;(reverse '(1 3 5 9 11))

(define (id obj) obj)
;;eqv?
(define eqv? eq?)


;===================trace================
;;trace
(define trace-display 
    display)

(define (print-level indent char)
    (if (< indent 1) (trace-display "")
	(begin
	 (trace-display char)
	 (print-level (- indent 1) char))))

;(print-level 10 "--")

;(define nil '())

(define *trace-functions* '())
(define *trace-level* 1)
(define *trace-state* 0)
(define (find-trace-function name functions)
    (if (null? functions)
	'()
	(if (eq? (caar functions) name)
	    (cdar functions)
	    (find-trace-function name (cdr functions)))))

(define (trace-invoke proc args)
;    (display args)                                                                                                                                        
;  (display (length args))                                                                                                                                 
;  (proc (car args)  ))
  (apply proc args))
;(cond ((<= 1 (length args))
;          (proc (car args) ))
;       ((= 2 (length args))
;        (proc (car args) (cadr args)))
;        ((= 3 (length args))
;       (proc (car args) (cadr args) (caddr args)))
;       ((>= 4 (length args))
;       (proc (car args) (cadr args) (caddr args) (cadddr args)))))

(define (trace name)
    (let ((proc (find-proc name))
	  (new-proc nil )
	  (proc-name (proc-name name))
	  (result nil))
      ;(display "proc=name:")
      ;(display proc-name)
      ;(newline)
      (if (eq? 'trace-display proc-name)
	  (set! proc-name 'display))
;      (display proc-name)

      (set! new-proc (lambda x
		       (begin
			(print-level *trace-level* "-")    
			(trace-display proc-name)
			(trace-display  x)
					;(map (lambda (a) (display a) (newline)  )x)
			(trace-display "\n")
			(set! *trace-level* (+ *trace-level* 1))
					;(set! result (proc (car x) (car (cdr x)) ))
			(set! result (trace-invoke  proc  x ))
			(set! *trace-level* (- *trace-level* 1)) 
			(print-level *trace-level* "-")
			(trace-display result)
			(trace-display "\n")
			result)) )
					;(display (cons proc new-proc))
      (update-proc proc-name new-proc)))

;set sys-log level
;(sys:set-log-level 7);for debug log

;;list*
(define (list* . args)
  (if (pair? args)
      (if (pair? (cdr args))
          (cons (car args) (apply list* (cdr args)))
          (car args) )
      (quote ()) ) )


;;last-pair
(define (last-pair l)
  (if (pair? l)
      (if (pair? (cdr l)) 
          (last-pair (cdr l))
          l )
      (error 'last-pair l) ) )


;;reverse!
(define (reverse! l)
  (define (nreverse l r)
    (if (pair? l)
       (let ((cdrl (cdr l)))
         (set-cdr! l r)
         (nreverse cdrl l) )
       r ) )
  (nreverse l '()) )
;;list-length
(define list-length
    (lambda (lst)
      (if (null? lst)
	  0
	  (+ 1 (list-length (cdr lst))))))

;;nth-element
(define nth-element
    (lambda (lst n)
      (if (null? lst)
	  (erro "report-list-too-short" n)
	  (if (zero? n)
	      (car lst)
	      (nth-element (cdr lst) (- n 1))))))
;;remove-first
(define remove-first
    (lambda (s los)
      (if (null? los)
	  '()
	  (if (eqv? (car los) s)
	      (cdr los)
	      (cons (car los) (remove-first s (cdr los)))))))

;;occurs-free?
(define occurs-free?
    (lambda (var exp)
      (cond
	((symbol? exp) (eqv? var exp))
	((eqv? (car exp) 'lambda)
	 (and
	  (not (eqv? var (car (cadr exp))))
	  (occurs-free? var (caddr exp))))
	(else (or
	       (occurs-free? var (car exp))
	       (occurs-free? var (cadr exp)))))))
;;subst
(define subst
    (lambda (new old slist)
      (if (null? slist)
	  '() (cons
	       (subst-in-s-exp new old (car slist)) 
	       (subst new old (cdr slist))))))

(define subst-in-s-exp
    (lambda (new old sexp)
      (if (symbol? sexp)
	  (if (eqv? sexp old) new sexp)
	  (subst new old sexp))))

;;remainder
(define (remainder n d)
    (if (< n d)
	n
	(remainder (- n d) d)))
;;gcd
(define (gcd a b)
    (if (= b 0)
	a
	(gcd b (remainder a b))))

;;list-ref
(define (list-ref items n)
    (if (= n 0)
	(car items)
	(list-ref (cdr items) (- n 1))))

