const EventBus = {
  $on(eventType: string, callback: (arg0: any) => any) {
    document.addEventListener(eventType, (ev) => callback(ev));
  },

  $dispatch(eventType: string, data: Object) {
    const event = new CustomEvent(eventType, { detail: data });
    document.dispatchEvent(event);
  },

  $remove(eventType: string, callback: EventListenerOrEventListenerObject) {
    document.removeEventListener(eventType, callback);
  },
};

export default EventBus;
