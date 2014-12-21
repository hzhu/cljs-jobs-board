// Compiled by ClojureScript 0.0-2280
goog.provide('main.core');
goog.require('cljs.core');
goog.require('firebase.session');
goog.require('reagent.core');
goog.require('ajax.core');
goog.require('ajax.core');
goog.require('firebase.session');
goog.require('firebase.session');
goog.require('reagent.core');
goog.require('reagent.core');
main.core.on_change = (function on_change(event,fb){fb.set(cljs.core.clj__GT_js.call(null,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"text-from-app","text-from-app",-1807524948),event.target.value], null)));
return fb.on("value",(function (snapshot){return firebase.session.global_put_BANG_.call(null,new cljs.core.Keyword(null,"my-text","my-text",1588438831),cljs.core.js__GT_clj.call(null,snapshot.val()).call(null,"text-from-app"));
}));
});
main.core.input_field = (function input_field(value,fb){return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"type","type",1174270348),"text",new cljs.core.Keyword(null,"value","value",305978217),value,new cljs.core.Keyword(null,"on-change","on-change",-732046149),(function (p1__5231_SHARP_){return main.core.on_change.call(null,p1__5231_SHARP_,fb);
})], null)], null);
});
main.core.app_view = (function app_view(){var fb = (new Firebase("https://jobs-board.firebaseio.com/"));return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"h2","h2",-372662728),"Home PAge!"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"p","p",151049309),"The value is now: ",firebase.session.global_state.call(null,new cljs.core.Keyword(null,"my-text","my-text",1588438831))], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"p","p",151049309),"Change it here: ",new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [main.core.input_field,firebase.session.global_state.call(null,new cljs.core.Keyword(null,"my-text","my-text",1588438831)),fb], null)], null)], null)], null);
});
reagent.core.render_component.call(null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [main.core.app_view], null),document.getElementById("app"));

//# sourceMappingURL=core.js.map