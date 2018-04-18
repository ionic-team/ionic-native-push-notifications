importScripts('https://www.gstatic.com/firebasejs/4.8.1/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/4.8.1/firebase-messaging.js');

let myId = get_sw_url_parameters( 'messagingSenderId' );

function get_sw_url_parameters( param ) {
  let vars = {};
  // @ts-ignore
  self.location.href.replace( self.location.hash, '' ).replace(/[?&]+([^=&]+)=?([^&]*)?/gi, function( m, key, value ) {vars[key] = value !== undefined ? value : '';});
  if( param ) {
    return vars[param] ? vars[param] : null;
  }
  return vars;
}

firebase.initializeApp({
  'messagingSenderId': myId
});

let messaging = firebase.messaging();

messaging.setBackgroundMessageHandler(function(payload) {
  let ev1 = new CustomEvent('pushOnBackgroundNotification', {detail: payload});
  window.dispatchEvent(ev1);
});
