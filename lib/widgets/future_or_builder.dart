import 'dart:async';

import 'package:avancer/widgets/dots_loading_indicator.dart';
import 'package:flutter/widgets.dart';

// ignore: must_be_immutable
class FutureOrBuilder<T> extends StatelessWidget {

  FutureOr<T> _future;
  Widget Function(BuildContext, T) _builder;

  Widget _loadingWidget;
  Widget _errorWidget;


  FutureOrBuilder({@required FutureOr<T> future, @required Widget Function(BuildContext, T) builder,
    Widget loadingWidget, Widget errorWidget}){

      this._future = future;
      this._builder = builder;

      this._loadingWidget = loadingWidget ?? DotsLoadingIndicator();
      this._errorWidget = errorWidget ?? Text('Error');

  }


  @override
  Widget build(BuildContext context) {
    
    if(_future is T){
      return _builder(context, _future);
    }

    return FutureBuilder<T>(
      future: _future as Future<T>,
      builder: (context, snap){

        if(snap.hasError){
          return _errorWidget;
        }

        if(snap.connectionState != ConnectionState.done){
          return _loadingWidget;
        }

        return _builder(context, snap.data);

      },
    );
    
  }

}