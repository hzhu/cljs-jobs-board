// Compiled by ClojureScript 0.0-2280
goog.provide('firebase.session');
goog.require('cljs.core');
goog.require('reagent.core');
goog.require('reagent.core');
goog.require('reagent.core');
cljs.core.enable_console_print_BANG_.call(null);
firebase.session.app_state = reagent.core.atom.call(null,new cljs.core.PersistentArrayMap(null, 1, ["post",new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"hostel_name","hostel_name",-461677165),"",new cljs.core.Keyword(null,"job_description","job_description",1478055288),"",new cljs.core.Keyword(null,"location","location",1815599388),"",new cljs.core.Keyword(null,"email","email",1415816706),"",new cljs.core.Keyword(null,"website","website",649297111),""], null)], null));
firebase.session.printAtom = (function printAtom(){cljs.core.println.call(null,"::::::::::ATOM::::::::::");
cljs.core.println.call(null,"::::::::::::::::::::::::");
cljs.core.println.call(null,cljs.core.get_in.call(null,cljs.core.deref.call(null,firebase.session.app_state),""));
cljs.core.println.call(null,"::::::::::::::::::::::::");
return cljs.core.println.call(null,"::::::::::::::::::::::::");
});
/**
* @param {...*} var_args
*/
firebase.session.global_state = (function() { 
var global_state__delegate = function (k,p__5315){var vec__5317 = p__5315;var default$ = cljs.core.nth.call(null,vec__5317,(0),null);return cljs.core.get.call(null,cljs.core.deref.call(null,firebase.session.app_state),k,default$);
};
var global_state = function (k,var_args){
var p__5315 = null;if (arguments.length > 1) {
  p__5315 = cljs.core.array_seq(Array.prototype.slice.call(arguments, 1),0);} 
return global_state__delegate.call(this,k,p__5315);};
global_state.cljs$lang$maxFixedArity = 1;
global_state.cljs$lang$applyTo = (function (arglist__5318){
var k = cljs.core.first(arglist__5318);
var p__5315 = cljs.core.rest(arglist__5318);
return global_state__delegate(k,p__5315);
});
global_state.cljs$core$IFn$_invoke$arity$variadic = global_state__delegate;
return global_state;
})()
;
firebase.session.global_put_BANG_ = (function global_put_BANG_(k,v){cljs.core.println.call(null,"this is K. The new item swapped into the atom.");
cljs.core.println.call(null,k);
return cljs.core.swap_BANG_.call(null,firebase.session.app_state,cljs.core.assoc,k,v);
});
firebase.session.local_put_BANG_ = (function local_put_BANG_(a,k,v){return cljs.core.swap_BANG_.call(null,a,cljs.core.assoc,k,v);
});

//# sourceMappingURL=session.js.map