import 'package:avancer/others/remote_config_api.dart';
import 'package:flutter/material.dart';
import 'package:intro_slider/intro_slider.dart';
import 'package:intro_slider/slide_object.dart';


class WelcomeScreen extends StatelessWidget {
  
  @override
  Widget build(BuildContext context) {

    var rc = RemoteConfigApi.instance();
    var screenHeight = MediaQuery.of(context).size.height;

    // A linear adjustment is made with the image margin, so that the text fits on every screen
    // The values have been obtained so that there is a 70p/50p top/bottom margin on a 733p of height screen
    // and a 0p/0p top/bottom margin on a 533p of height screen

    var topImageMargin = 0.35*screenHeight - 186.55;
    var bottomImageMargin = 0.25*screenHeight - 133.25;

    return IntroSlider(
      slides: [
        Slide(
          description:
          "Avancer te permite retirar hasta el ${rc.salaryFraction*100}% de tu salario ya ganado de manera inmediata." +
          " A medida que pasan los días, vas a tener más dinero disponible para retirar." +
          " La tarifa es fija y del ${rc.lowInterest*100}%",
          pathImage: "assets/welcome_images/savings.png",
          backgroundColor: Theme.of(context).primaryColor,
          marginTitle: EdgeInsets.fromLTRB(0, topImageMargin, 0, bottomImageMargin)
        ),

        Slide(
          description: "Tenés hasta el día posterior al cobro de tu salario para hacer la devolución." +
          " No cobramos intereses tardíos, pero el no cumplimiento de los plazos afecta negativamente tu calificación crediticia.",
          pathImage: "assets/welcome_images/calendar.png",
          backgroundColor: Theme.of(context).primaryColor,
          marginTitle: EdgeInsets.fromLTRB(0, topImageMargin, 0, bottomImageMargin)
        ),

        Slide(
          description: "Podés devolver le préstamo a través de transferencia bancaria o por Mercado Pago",
          pathImage: "assets/welcome_images/payments.png",
          backgroundColor: Theme.of(context).primaryColor,
          marginTitle: EdgeInsets.fromLTRB(0, topImageMargin, 0, bottomImageMargin)
        )
      ],
      onDonePress: () => Navigator.of(context).pop(),
      isShowSkipBtn: false,
    );
  }

}
