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
          "Avancer te permite retirar hasta el ${rc.salaryFraction*100}% de tu salario ya ganado." +
          " A medida que pasan los días, vas a tener más dinero disponible para retirar.",
          pathImage: "assets/welcome_images/savings.png",
          backgroundColor: Theme.of(context).primaryColor,
          marginTitle: EdgeInsets.fromLTRB(0, topImageMargin, 0, bottomImageMargin)
        ),

        Slide(
          description: "Tenés hasta el día posterior al cobro de tu salario para hacer la devolución." +
          " Si solicitas un adelanto hasta 4 días antes del cobro de tu salario, la fecha de devolución cambia para el próximo mes. "
          +" Todo te lo va a indicar la app.",
          pathImage: "assets/welcome_images/calendar.png",
          backgroundColor: Theme.of(context).primaryColor,
          marginTitle: EdgeInsets.fromLTRB(0, topImageMargin, 0, bottomImageMargin)
        ),

        Slide(
          description: "A medida que uses la app, y devuelvas lo solicitado en tiempo y forma, tu tarifa será cada vez más baja.",
          pathImage: "assets/welcome_images/calculator.png",
          backgroundColor: Theme.of(context).primaryColor,
          marginTitle: EdgeInsets.fromLTRB(0, topImageMargin, 0, bottomImageMargin)
        ),

        Slide(
          description: "El monto del adelanto se debita de tu cuenta en la fecha de vencimiento. También podés precancelarlo antes de dicha fecha, a través de transferencia bancaria o Rapipago",
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
