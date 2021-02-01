import 'package:avancer/models/deposit_method.dart';
import 'package:avancer/models/user.dart';
import 'package:avancer/others/error_logger.dart';
import 'package:avancer/others/remote_config_api.dart';
import 'package:avancer/others/utils.dart';
import 'package:avancer/widgets/dots_loading_indicator.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';

import 'new_deposit_method_dialog.dart';

class ProfileScreen extends StatelessWidget {

  /* ----------------------------------------------------------------------------------------------------------------------
                                              BUILD METHODS
  ---------------------------------------------------------------------------------------------------------------------- */

  @override
  Widget build(BuildContext context) {

    Utils.checkInternetConnection(context);

    if(! Provider.of<User>(context, listen: false).isSignedIn()){
      Navigator.of(context).pushNamed('/signin');
    }

    return Scaffold(

      appBar: AppBar(
        leading: Image.asset('assets/logo_small.png',),
        title: Text('Tu Cuenta'),
        actions: <Widget>[
          IconButton(
            icon: Icon(Icons.arrow_back),
            onPressed: (){
              Navigator.of(context).pop();
            }
          )
        ],
      ),

      body: SingleChildScrollView(
        child: Consumer<User>(
          builder: (context, user, child) => Column(
            children: [

              Card(
                margin: EdgeInsets.all(0),
                child: Column(
                  children: <Widget>[

                    Stack(
                      children: <Widget>[
                        Image.asset(
                          'assets/waves_top.png',
                          width: MediaQuery.of(context).size.width,
                        ),

                        Align(
                          alignment: Alignment.center,
                          child: buildWelcomeSection(context, user),
                        )
                      ],
                    ),

                    buildAccountManagementSection(context, user)

                  ],
                ),
              ),

              buildPaymentMethodsSection(context, user),

              buildSupportSection(context)
              
            ]
          ),
        ),
      )

    );
  }


  Widget buildWelcomeSection(BuildContext context, User user) {

    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [

          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Text(
              '¡Hola!',
              style: Theme.of(context).textTheme.headline6.copyWith(color: Colors.white)
            ),
          ),

          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Text(
              '${user.getName()} ${user.getSurname()}',
              style: Theme.of(context).textTheme.headline5.copyWith(color: Colors.white)
            ),
          )
          
        ]
      ),
    );
  }


  Widget buildAccountManagementSection(BuildContext context, User user) {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[

          FlatButton(
            textColor: Theme.of(context).primaryColor,
            child: Text('CAMBIAR SUELDO'),
            onPressed: (){
              Utils.showSimpleDialog(context,
                title: "Cambio de sueldo",
                description: "Enviá tu último recibo de sueldo a contacto@avancer.com.ar,"
                  +" te avisaremos cuando hayamos procesado el cambio.",
                dismissable: true
              );
            },
          ),

          FlatButton(
            textColor: Theme.of(context).primaryColor,
            child: Text('CAMBIAR CONTRASEÑA'),
            onPressed: (){
              updatePassword(context, user);
            },
          ),

          FlatButton(
            textColor: Theme.of(context).primaryColor,
            child: Text('CERRAR SESIÓN'),
            onPressed: (){
              signOut(context, user);
            },
          ),

          FlatButton(
            textColor: Theme.of(context).primaryColor,
            child: Text('ELIMINAR CUENTA'),
            onPressed: (){
              removeAccount(context, user);
            },
          )

        ],
      ),
    );
  }


  Widget buildPaymentMethodsSection(BuildContext context, User user) {

    return Consumer<User>(
      builder: (context, user, widget){
        return Padding(
          padding: const EdgeInsets.fromLTRB(4, 8, 4, 8),
          child: Card(
            child: FutureBuilder<List<DepositMethod>>(
              future: user.getDepositMethods(),
              builder: (context, snapshot){

                if(snapshot.connectionState != ConnectionState.done){
                  return Container(
                    width: double.infinity,
                    child: DotsLoadingIndicator(),
                  );
                }

                var depositMethods = snapshot.data;

                return Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [

                      Align(
                        alignment: Alignment.centerLeft,
                        child: Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: Text(
                            'Tus medios de depósito',
                            style: Theme.of(context).textTheme.subtitle1,
                          ),
                        ),
                      ),

                      Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: depositMethods.isNotEmpty

                          ? ListView.builder(
                              shrinkWrap: true,
                              itemBuilder: (context, i) => buildListTile(depositMethods[i], user, context),
                              itemCount: depositMethods.length,
                            )

                          : Text('No tienes ninguno actualmente')
                      ),

                      FlatButton(
                        child: Text('Agregar uno'),
                        textColor: Theme.of(context).primaryColor,
                        onPressed: () => addDepositMethod(context, user)
                      )

                    ]
                  ),
                );

              }
            ),
          ),
        );
      }
    );
  }


  Widget buildListTile(DepositMethod depositMethod, User user, BuildContext context) {

    return ListTile(
      leading: Icon(
        Icons.attach_money,
      ),

      title: Text(
        depositMethod.getName()
      ),

      subtitle: Text(
        depositMethod.getDescription()
      )

    );

  }


  Widget buildSupportSection(BuildContext context){
    return Padding(
      padding: const EdgeInsets.fromLTRB(4, 8, 4, 8),
      child: Card(
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[

              Padding(
                padding: const EdgeInsets.all(8.0),
                child: Text(
                  'Soporte',
                  style: Theme.of(context).textTheme.subtitle1,
                ),
              ),

              Padding(
                padding: const EdgeInsets.all(8.0),
                child: Text('Si tenés algún inconveniente, no dudes en contactarnos'),
              ),

              Padding(
                padding: const EdgeInsets.all(8.0),
                child: Text(RemoteConfigApi.instance().contactEmail),
              )

            ],
          ),
        ),
      ),
    );

  }


  Widget buildUpdatePasswordDialog(BuildContext context){

    var _currentPassInputKey = GlobalKey<FormFieldState>();
    var _newPassInputKey = GlobalKey<FormFieldState>();
    var _newPassConfirmInputKey =GlobalKey<FormFieldState>();

    return Utils.buildColumnDialog(
      children: <Widget>[

        Padding(
          padding: const EdgeInsets.all(8.0),
          child: TextFormField(
            key: _currentPassInputKey,
            obscureText: true,
            decoration: InputDecoration(
              hintText: 'Contraseña actual'
            ),
          ),
        ),

        Padding(
          padding: const EdgeInsets.all(8.0),
          child: TextFormField(
            key: _newPassInputKey,
            decoration: InputDecoration(
              hintText: 'Nueva contraseña'
            ),
            obscureText: true,
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
        ),

        Padding(
          padding: const EdgeInsets.all(8.0),
          child: TextFormField(
            key: _newPassConfirmInputKey,
            decoration: InputDecoration(
              hintText: 'Confirmá la nueva contraseña'
            ),
            obscureText: true,
            validator: (password){

              if(password != _newPassInputKey.currentState.value)
                return 'Las contraseñas deben ser iguales';

              return null;
              
            }
          ),
        ),

        Padding(
          padding: const EdgeInsets.all(8.0),
          child: FlatButton(
            child: Text('Cambiar contraseña'),
            onPressed: () {
              if(_newPassInputKey.currentState.validate() && _newPassConfirmInputKey.currentState.validate()){
                Navigator.of(context).pop(<String,String>{
                  'current': _currentPassInputKey.currentState.value,
                  'new': _newPassInputKey.currentState.value
                });
              }
            },
          ),
        )

      ]
    );
  }


  Widget buildRemoveAccountDialog(BuildContext context) {

    var _passwordKey = GlobalKey<FormFieldState>();

    return AlertDialog(
      title: Text('¿Seguro que deseas eliminar tu cuenta?'),
      content: TextFormField(
        key: _passwordKey,
        obscureText: true,
        decoration: InputDecoration(
          hintText: 'Tu contraseña'
        ),
      ),
      actions: <Widget>[

        FlatButton(
          child: Text('Eliminar cuenta'),
          onPressed: () {
            Navigator.of(context).pop(<String,dynamic>{
              'password': _passwordKey.currentState.value,
              'remove': true
            });
          },
        ),

        FlatButton(
          child: Text('Cancelar'),
          onPressed: () {
            Navigator.of(context).pop(<String,dynamic>{
              'remove': false
            });
          },
        )

      ],
    );
  }


  /* ------------------------------------------------------------------------------------------------------------
                                            ACTIONS METHODS
  ------------------------------------------------------------------------------------------------------------ */


  Future<void> updatePassword(BuildContext context, User user) async {
    
    var passwords = await showDialog<Map<String,String>>(
      context: context,
      builder: (context){
        return buildUpdatePasswordDialog(context);
      },
    );

    if(passwords == null)
      return;

    Utils.showSnackbar('Cambiando contraseña', context);

    try{

      await user.signIn(user.getEmail(), passwords['current']);

      try{
      
        await user.updatePassword(passwords['new']);

        Utils.showSnackbar('Contraseña cambiada', context);

      }catch(e){

        Utils.showSnackbar('Ha ocurrido un error', context);

      }

    }catch(error){

      PlatformException _exception = error;
      String _failureReason;

      switch (_exception.code) {
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
          ErrorLogger.log(context: 'Re-signing in for changing passwords', error: error.toString(), userUID: user.getId());
          _failureReason = 'Ha surgido un error';
          break;
      }

      Utils.showSnackbar(_failureReason, context);

    }

  }


  Future<void> signOut(BuildContext context, User user) async {

    Utils.showSnackbar('Cerrando sesión', context);
   
    try{
    
      await user.signOut();

      Scaffold.of(context).hideCurrentSnackBar();

      Navigator.of(context).pushReplacementNamed('/signin');

    }catch(e){

      Utils.showSnackbar('Ha ocurrrido un error', context);

    }

  }


  Future<void> removeAccount(BuildContext context, User user) async {
  
    var params = await showDialog<Map<String,dynamic>>(
      context: context,
      builder: (context){
        return buildRemoveAccountDialog(context);
      },
    );

    if(params == null || params['remove'] == false)
      return;

    Utils.showSnackbar('Eliminando cuenta', context);

    try{

      await user.signIn(user.getEmail(), params['password']);

      try{
      
        bool removed = await user.deleteAccount();

        if(removed){

           Navigator.of(context).pushNamed('/signin');
          
        }else{

          await showDialog(
            context: context,
            child: AlertDialog(
              title: Text('ATENCIÓN'),
              content: Text('Debido a que poseés deuda, tu cuenta no ha sido eliminada. Un administrador revisará tu solicitud'
                +' y se contactará con vos próximamente'),
            )
          );

        }


      }catch(e){

        ErrorLogger.log(context: 'Removing account', error: e.toString(), userUID: user.getId());
        Utils.showSnackbar('Ha surgido un error', context);

      }

    }catch(error){

      PlatformException _exception = error;
      String _failureReason;

      switch (_exception.code) {
        case 'ERROR_WRONG_PASSWORD':
          _failureReason = 'Contraseña erronea';
          break;

        default:
          ErrorLogger.log(context: 'Re-signing in for account removal', error: error.toString(), userUID: user.getId());
          _failureReason = 'Ha surgido un error';
          break;
      }

      Utils.showSnackbar(_failureReason, context);

    }

  }


  Future<void> addDepositMethod(BuildContext context, User user) async {

    var depositMethod = await showDialog<DepositMethod>(
      context: context,
      builder: (context) => NewDepositMethodScreen()
    );

    if(depositMethod == null)
      return;

    Utils.showSnackbar('Agregando medio', context);

    try{

      await user.addDepositMethod(depositMethod);

      Utils.showSnackbar('Medio agregado', context);

    }catch(error){

      ErrorLogger.log(context: 'Adding deposit method', error: error.toString(), userUID: user.getId());
      Utils.showSnackbar('Ha surgido un error', context);

    }

  }


}