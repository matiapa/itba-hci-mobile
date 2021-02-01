import 'package:avancer/models/user.dart';
import 'package:avancer/others/error_logger.dart';
import 'package:avancer/others/utils.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:multi_image_picker/multi_image_picker.dart';
import 'package:provider/provider.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:flutter_credit_card/flutter_credit_card.dart';


class SignUpScreen extends StatefulWidget {

  @override
  _SignUpScreenState createState() => _SignUpScreenState();
}

class _SignUpScreenState extends State<SignUpScreen> {

  final _formKey = GlobalKey<FormState>();
  final _ccFormKey =  GlobalKey<FormState>();

  String email="", password="", phone="";
  int paymentDay = 0;

  List<Asset> images = List<Asset>();

  CreditCardModel _ccModel = CreditCardModel('', '', '', '', false);
  bool validCardAdded = false;

  @override
  void initState() {
    Utils.checkInternetConnection(context);
    super.initState();
  }


  @override
  Widget build(BuildContext context) {
    
    return Scaffold(

      appBar: AppBar(
        leading: Image.asset('assets/logo_small.png',),
        title: Text('Avancer'),
      ),

      body: Builder(
        builder: (scaffoldContext){
          return Container(
            width: double.infinity,
            height: double.infinity,
            alignment: Alignment.center,
            child: Container(
              height: 600,
              width: 300,
              child: Card(
                child: Padding(
                  padding: const EdgeInsets.fromLTRB(32, 16, 32, 16),
                  child: buildForm(scaffoldContext),
                ),
              ),
            ),
          );
        }
      )

    );
  }


  Widget buildForm(BuildContext scaffoldContext) {

    return Form(
      key: _formKey,
      child: ListView(
        shrinkWrap: true,
        children: [

          ...buildBasicInfoSection(),

          ...buildDocumentsSection(),

          ...buildCreditCardSection(),

          ...buildSignUpSection(scaffoldContext)

        ]
      ),
    );
  }


  List<Widget> buildBasicInfoSection(){
    return [
      Padding(
        padding: const EdgeInsets.symmetric(vertical: 12.0),
        child: TextFormField(
          decoration: InputDecoration(
            labelText: 'Tu email',
          ),
          validator: validateEmail,
          onChanged: (value) => email = value,
        )
      ),

      Padding(
        padding: const EdgeInsets.symmetric(vertical: 12.0),
        child: TextFormField(
          obscureText: true,
          decoration: InputDecoration(
            labelText: 'Tu contraseña'
          ),
          validator: validatePassword,
          onChanged: (value) => password = value,
        ),
      ),

      Padding(
        padding: const EdgeInsets.symmetric(vertical: 12.0),
        child: TextFormField(
          decoration: InputDecoration(
            labelText: 'Tu teléfono',
          ),
          keyboardType: TextInputType.number,
          validator: validatePhone,
          onChanged: (value) => phone = value,
        ),
      ),

      Padding(
        padding: const EdgeInsets.symmetric(vertical: 12.0),
        child: TextFormField(
          keyboardType: TextInputType.number,
          decoration: InputDecoration(
            labelText: 'Día del mes que cobrás',
          ),
          validator: (v) => validatePaymentDay(int.parse(v)),
          onChanged: (v) => paymentDay = int.parse(v),
        ),
      )
    ];
  }


  List<Widget> buildDocumentsSection(){
    return [
      Padding(
        padding: const EdgeInsets.all(8.0),
        child: Divider(),
      ),

      Padding(
        padding: const EdgeInsets.symmetric(vertical: 12.0),
        child: Text("Fotos de tu DNI (dorso y reverso) y último recibo de sueldo"),
      ),

      images!=null && images.isNotEmpty
        ? Padding(
            padding: const EdgeInsets.symmetric(vertical: 12.0),
            child: Container(
              width: double.infinity,
              height: 100,

              child: GridView.count(
                crossAxisCount: 3,

                children: List.generate(images.length, (index) {
                  Asset asset = images[index];
                  return AssetThumb(
                    asset: asset,
                    width: 300,
                    height: 300,
                  );
                }),

              )

            ),
          )
        : Container(),

      Padding(
        padding: const EdgeInsets.symmetric(vertical: 12.0),
        child: RaisedButton(
          child: Text("Elegir imágenes"),
          onPressed: () => loadAssets(),
        ),
      )
    ];

  }


  List<Widget> buildCreditCardSection(){
    return [
      Padding(
        padding: const EdgeInsets.all(8.0),
        child: Divider(),
      ),

      Padding(
        padding: const EdgeInsets.all(8.0),
        child: Text('Para poder debitar los préstamos, neceistamos que agregues una tarjeta de crédito'),
      ),

      CreditCardWidget(
          cardNumber: _ccModel.cardNumber,
          expiryDate: _ccModel.expiryDate, 
          cardHolderName: _ccModel.cardHolderName,
          cvvCode: _ccModel.cvvCode,
          showBackView: _ccModel.isCvvFocused,
          obscureCardCvv: false,
          obscureCardNumber: false,
      ),

      RaisedButton(
        child: Text('🔒 Agregar tarjeta de débito'),
        onPressed: !validCardAdded
          ? (){
            showDialog(
              context: context,
              child: buildCreditCardDialog(),
            );
          }
          : null
      ),
    ];
  }


  Widget buildCreditCardDialog() {
    return AlertDialog(
      title: Text('Nueva tarjeta de débito'),
      content: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Text('Se te debitarán \$50 pesos para validar tu tarjeta, y se te devolverán de inmediato'),
          ),

          CreditCardForm(
            formKey: _ccFormKey,
            onCreditCardModelChange: (CreditCardModel data) {
              setState(() {
                _ccModel = data;
              });
            },
            obscureCvv: true, 
            cardNumberDecoration: const InputDecoration(
              border: OutlineInputBorder(),
              labelText: 'Número',
              hintText: 'XXXX XXXX XXXX XXXX',
            ),
            numberValidationMessage: 'No válido',
            expiryDateDecoration: const InputDecoration(
              border: OutlineInputBorder(),
              labelText: 'Vencimiento',
              hintText: 'XX/XX',
            ),
            dateValidationMessage: 'No válido',
            cvvCodeDecoration: const InputDecoration(
              border: OutlineInputBorder(),
              labelText: 'CVV',
              hintText: 'XXX',
            ),
            cvvValidationMessage: 'No válido',
            cardHolderDecoration: const InputDecoration(
              border: OutlineInputBorder(),
              labelText: 'Titular',
            ),
          ),

          Padding(
            padding: const EdgeInsets.all(8.0),
            child: RaisedButton(
              child: Text('Agregar'),
              onPressed: (){
                if(_ccFormKey.currentState.validate()){
                  setState(() {
                    validCardAdded = true;
                  });
                  Navigator.of(context).pop();
                }
              },
            ),
          )
        ],
      ),
    );
  }


  List<Widget> buildSignUpSection(BuildContext scaffoldContext){
    return [
      Padding(
        padding: const EdgeInsets.all(8.0),
        child: Divider(),
      ),

      Padding(
        padding: const EdgeInsets.symmetric(vertical: 8.0),
        child: Text('Al registrarte aceptás los:'),
      ),

      FlatButton(
        child: Text(
          'Términos y Condiciones',
          style: Theme.of(context).textTheme.button.copyWith(color: Colors.black)
        ),
        onPressed: () => launch('https://www.avancer.com.ar/terminos-y-condiciones'),
      ),

      RaisedButton(                    
        child: Text('Registrarse'),
        onPressed: (){
          signUp(scaffoldContext);
        }
      )

    ];
  }


  Future<void> loadAssets() async {
    try {

      List<Asset> resultList = await MultiImagePicker.pickImages(
        maxImages: 3,
      );

      setState(() {
        images = resultList;
      });

    } on Exception {
      
      setState(() {
        images = new List();
      });

    }
  }


  // ----------------------------------------------- FORM VALIDATORS ------------------------------------------

  String validateEmail(value) {
    if (value==null || value.isEmpty)
      return 'Debes ingresar un email';
      
    return null;
  }


  String validatePassword(value) {
    if(value==null || value.length < 8)
      return 'Debe contener al menos 8 caracteres';

    if(! value.contains(new RegExp(r'[A-Z]')) || ! value.contains(new RegExp(r'[a-z]')))
      return 'Debe contener mayúsculas y minúsculas';

    if(! value.contains(new RegExp(r'[0-9]')))
      return 'Debe contener al menos un dígito';

    return null;
  }


  String validatePhone(value) {
    if (value==null || value.length < 6)
      return 'Debes ingresar tu teléfono';

    return null;
  }


  String validatePaymentDay(value) {
    if (value==null)
      return 'Debes ingresar tu día de cobro';

    if (value < 1 || value > 31)
      return 'Debes ingresar un número entre 1 y 31';

    return null;    
  }


  // ----------------------------------------------- SIGN UP HANDLER ------------------------------------------


  Future<void> signUp(BuildContext scaffoldContext) async {

    var _user = Provider.of<User>(context, listen: false);

    // Dismiss the keyboard

    FocusScope.of(context).unfocus();

    // Validate form

    if (! _formKey.currentState.validate())
      return;

    // if(images == null || images.length != 3){
    //   Utils.showSnackbar("Debes ingresar las 3 imágenes solicitadas", scaffoldContext);
    //   return;
    // }

    if(!validCardAdded){
      Utils.showSnackbar("Debes ingresar una tarjeta de crédito", scaffoldContext);
      return;
    }

    // Sign up

    try{

      Utils.showLoadingDialog(scaffoldContext, title: 'Creando cuenta', description: 'Por favor espera');

      email = email.trim();
      password = password.trim();
      
      await _user.signUp(
        email, password, phone, paymentDay, images,
        _ccModel.brand, _ccModel.cardNumber, _ccModel.cvvCode, _ccModel.expiryDate, _ccModel.cardHolderName
      );

      Navigator.of(scaffoldContext).pop();

      Utils.showSimpleDialog(scaffoldContext,
        title: "Listo",
        description: "En breve verificaremos tu cuenta y podrás empezar a utilizar Avancer."
      );

    }catch(error){

      String _failureReason;

      if(error is PlatformException){
        PlatformException _exception = error;

        switch (_exception.code) {
          case 'ERROR_INVALID_EMAIL':
            _failureReason = 'Email inválido';
            break;

          case 'ERROR_WEAK_PASSWORD':
            _failureReason = _exception.details;
            break;

          case 'ERROR_EMAIL_ALREADY_IN_USE':
            _failureReason = 'Email ya existente';
            break;

          default:
            _failureReason = 'Ha surgido un error';
            ErrorLogger.log(context: 'Signing in', error: "$_failureReason: ${_exception.message}");
            break;
        }
      }else{

        _failureReason = "Error inesperado";
        
        ErrorLogger.log(context: 'Signing in', error: "Unexpected error");

      }

      Navigator.of(scaffoldContext).pop();
      Utils.showSnackbar(_failureReason, scaffoldContext);

      return;

    }

  }

}