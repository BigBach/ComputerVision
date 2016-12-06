package gui;

class Holder<T> {
        private T value;
 
        Holder(T value) {
            setValue(value);
        }
 
        T getValue() {
            return value;
        }
 
        void setValue(T value) {
            this.value = value;
        }
    }