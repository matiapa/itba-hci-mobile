import 'package:avancer/models/user.dart';
import 'package:avancer/others/error_logger.dart';
import 'package:avancer/others/utils.dart';
import 'package:avancer/screens/signup_screen.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';

class SignInScreen extends StatefulWidget {

  @override
  _SignInScreenState createState() => _SignInScreenState();
}

class _SignInScreenState extends State<SignInScreen> {

  final _emailInputController = TextEditingController();
  final _passwordInputController = TextEditingController();

  @override
  void initState() {
    Utils.checkInternetConnection(context);

    _emailInputController.addListener(() {
      setState((){});
    });
    
    super.initState();
  }


  @override
  Widget build(BuildContext context) {

    var keyboardOff = MediaQuery.of(context).viewInsets.bottom == 0;
    var screenHeight = MediaQuery.of(context).size.height;
    
    return Scaffold(

      appBar: AppBar(
        leading: Image.asset('assets/logo_small.png',),
        title: Text('Avancer'),
      ),

      body: Builder(
        builder: (context){
          return Column(
            children: [

              Padding(
                padding: EdgeInsets.all(24),
                child: Container(
                  height: 400,
                  width: 290,
                  child: Card(
                    child: Padding(
                      padding: const EdgeInsets.fromLTRB(32, 16, 32, 16),
                      child: ListView(
                        
                        shrinkWrap: true,
                        children: [

                          Center(
                            child: Padding(
                              padding: const EdgeInsets.symmetric(vertical: 16.0),
                              child: Text(
                                '¡Bienvenido!',
                                style: Theme.of(context).textTheme.headline5
                              ),
                            ),
                          ),

                          Padding(
                            padding: const EdgeInsets.symmetric(vertical: 12.0),
                            child: TextField(
                              controller: _emailInputController,
                              decoration: InputDecoration(
                                hintText: 'Email'
                              ),
                            ),
                          ),

                          Padding(
                            padding: const EdgeInsets.symmetric(vertical: 12.0),
                            child: TextField(
                              controller: _passwordInputController,
                              decoration: InputDecoration(
                                hintText: 'Contraseña',
                              ),
                              obscureText: true,
                            ),
                          ),

                          Padding(
                            padding: const EdgeInsets.symmetric(vertical:16.0),
                            child: RaisedButton(                    
                              child: Text('Iniciar Sesión'),
                              onPressed: (){
                                signIn(context);
                              }
                            ),
                          ),


                          Padding(
                            padding: const EdgeInsets.symmetric(vertical:16.0),
                            child: RaisedButton(                    
                              child: Text('Registrarse'),
                              onPressed: (){
                                Navigator.of(context).push(new MaterialPageRoute(
                                  builder: (context) => new SignUpScreen()
                                ));
                              }
                            ),
                          ),

                        
                        ]
                      ),
                    ),
                  ),
                ),
              ),

              keyboardOff && screenHeight>600
              ? Expanded(
                child: Align(
                  alignment: Alignment.bottomCenter,
                  child: Image.asset(
                    'assets/waves_bottom.png',
                    width: MediaQuery.of(context).size.width,
                  ),
                ),
              )
              : Container()

            ]
          );
        }
      )

    );
  }


  AlertDialog buildUpdatePasswordDialog(BuildContext context) {

    var newPassInputKey = GlobalKey<FormFieldState>();
    var newPassConfirmInputKey = GlobalKey<FormFieldState>();

    return AlertDialog(
      content: Column(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[

          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Text('Te sugerimos cambiar tu contraseña'),
          ),

          TextFormField(
            key: newPassInputKey,
            obscureText: true,
            decoration: InputDecoration(
              hintText: 'Nueva contraseña'
            ),
            validator: (password){

              if(password.length < 8)
                return 'Debe contener al menos 8 caracteres';

              if(! password.contains(new RegExp(r'[A-Z]')) || ! password.contains(new RegExp(r'[a-z]')))
                return 'Debe contener mayúsculas y minúsculas';

              if(! password.contains(new RegExp(r'[0-9]')))
                return 'Debe contener al menos un dígito';

              return null;

              // bool hasSpecialCharacters = password.contains(new RegExp(r'[!@#$%^&*(),.?":{}|<>]'));
            }
          ),

          TextFormField(
            key: newPassConfirmInputKey,
            obscureText: true,
            decoration: InputDecoration(
              hintText: 'Confirmá tu nueva contraseña'
            ),
            validator: (password){

              if(password != newPassInputKey.currentState.value)
                return 'Las contraseñas deben ser iguales';

              return null;

            }
          ),

          FlatButton(
            child: Text('Cambiar contraseña'),
            onPressed: () {
              if(newPassInputKey.currentState.validate() && newPassConfirmInputKey.currentState.validate()){
                Navigator.of(context).pop(newPassInputKey.currentState.value);
              }
            },
          )

        ],
      ),
    );

  }


  Future<void> signIn(BuildContext context) async {

    var _user = Provider.of<User>(context, listen: false);

    // Dismiss the keyboard

    FocusScope.of(context).unfocus();

    // Sign in

    try{

      Utils.showLoadingDialog(context, title: 'Iniciando sesión', description: 'Por favor espera');

      _emailInputController.text = _emailInputController.text.trim();
      _passwordInputController.text = _passwordInputController.text.trim();

      await _user.signIn(
        _emailInputController.text.length>0 ? _emailInputController.text : 'email',
        _passwordInputController.text.length>0 ? _passwordInputController.text : 'pass'
      );

      // Check if user is approved

      if(! await _user.checkIfUserApproved()){

        await _user.signOut();

        await Utils.showSimpleDialog(context,
          title: "Tu cuenta tovadía no ha sido aprobada",
          description: "Te enviaremos un mail cuando ya puedas utilizar Avancer"
        );

      }


      // // Check if password must be changed

      try{

        if(await _user.checkIfPassMustBeChanged()){

          var newPass;

          while(newPass == null){
            newPass = await showDialog<String>(
              barrierDismissible: false,
              context: context,
              builder: (context){
                return buildUpdatePasswordDialog(context);
              },
            );
          }

          await _user.updatePassword(newPass);

        }

        // Dismiss dialog
        Navigator.of(context).pop();

        // Go back to main screen
        Navigator.of(context).pop();

        if(Navigator.of(context).canPop()){
          Navigator.of(context).pop();
        }

      }catch(e){

        Navigator.of(context).pop();
        Utils.showSnackbar('Ha surgido un error', context);

        ErrorLogger.log(context: 'Signing in', error: e.toString());
        return;

      }

    }catch(error){

      String _failureReason;

      if(error is PlatformException){
        PlatformException _exception = error;

        switch (_exception.code) {
          case 'ERROR_INVALID_EMAIL':
            _failureReason = 'Email inválido';
            break;

          case 'ERROR_WRONG_PASSWORD':
            _failureReason = 'Contraseña erronea';
            break;

          case 'ERROR_USER_NOT_FOUND':
            _failureReason = 'Email no encontrado';
            break;

          case 'ERROR_USER_DISABLED':
            _failureReason = 'Usuario deshabilitado';
            break;

          default:
            _failureReason = 'Ha surgido un error';
            ErrorLogger.log(context: 'Signing in', error: "$_failureReason: ${_exception.message}");
            break;
        }
      }else{
        _failureReason = 'Ha surgido un error inesperado';
        ErrorLogger.log(context: 'Signing in', error: "Unexpected error");
      }

      Navigator.of(context).pop();
      Utils.showSnackbar(_failureReason, context);

      return;

    }

  }

}