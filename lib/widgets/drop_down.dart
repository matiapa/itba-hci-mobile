import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class DropDown<T> extends StatefulWidget{

  final String hint;
  final Map<T, String> items;
  final String Function(T item) validator;
  final void Function(T item) onSaved;
  final void Function(T item) onChanged;
  final T initialValue;

  const DropDown({Key key, this.hint, @required this.items, this.validator, this.onSaved, this.onChanged, this.initialValue})
   : super(key: key);

  @override
  State<StatefulWidget> createState() => DropDownState<T>();

}


class DropDownState<T> extends State<DropDown<T>>{

  T value;

  @override
  Widget build(BuildContext context) {
    
    return DropdownButtonFormField<T>(

      key: GlobalKey<FormFieldState>(),

      icon: Icon(Icons.arrow_downward),
      iconSize: 24,
      elevation: 16,

      value: value ?? widget.initialValue,

      hint: Text(widget.hint ?? ''),

      items: widget.items?.entries?.map((entry){

        return DropdownMenuItem<T>(
          value: entry.key,
          child: Text(entry.value)
        );

      })?.toList(),

      validator: widget.validator,
      
      onChanged: (T newValue){
        setState((){ value = newValue; });
        widget.onChanged(newValue);
      },

      onSaved: widget.onSaved

    );

  }

}