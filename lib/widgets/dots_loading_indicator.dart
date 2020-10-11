import 'package:flutter/material.dart';

class DotsLoadingIndicator extends StatelessWidget{

  final double height;

  DotsLoadingIndicator({this.height=35});

  @override
  Widget build(BuildContext context) {
    return Image.asset(
      'assets/loading_dots_indicator.gif',
      height: height
    );
  }


}