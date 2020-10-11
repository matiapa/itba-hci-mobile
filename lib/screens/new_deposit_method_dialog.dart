import 'package:avancer/models/deposit_method.dart';
import 'package:avancer/others/utils.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class NewDepositMethodScreen extends StatefulWidget{

  final _formKey = GlobalKey<FormState>();

  @override
  _NewDepositMethodScreenState createState() => _NewDepositMethodScreenState();

}

class _NewDepositMethodScreenState extends State<NewDepositMethodScreen> {

  String _name, _cbu, _cuit, _email;

  DepositMethodType type = DepositMethodType.BANK;

  /* ------------------------------------------------------------------------------------------------------------
                                            BUILD METHODS
  ------------------------------------------------------------------------------------------------------------ */

  @override
  Widget build(BuildContext context) {

    final Map<DepositMethodType, String> _mappedTypes = {
      DepositMethodType.BANK: 'Cuenta Bancaria',
      DepositMethodType.MERCADOPAGO: 'MercadoPago'
    };
    
    return Utils.buildColumnDialog(
      children: [

        Align(
          alignment: Alignment.centerLeft,
          child: Padding(
            padding: const EdgeInsets.all(8.0),
            child: Text(
              'Elegí un medio de depósito',
              style: Theme.of(context).textTheme.subtitle1,
            ),
          ),
        ),

        Padding(
          padding: const EdgeInsets.all(8.0),
          child: DropdownButtonFormField<DepositMethodType>(

            icon: Icon(Icons.arrow_downward),
            iconSize: 24,
            elevation: 16,

            value: type,

            items: _mappedTypes.entries.map((entry){
              return DropdownMenuItem<DepositMethodType>(
                value: entry.key,
                child: Text(entry.value)
              );
            })?.toList(),
            
            onChanged: (DepositMethodType newValue) => setState((){ type = newValue; }),

          ),
        ),

        Padding(
          padding: const EdgeInsets.all(8.0),
          child: type == DepositMethodType.BANK
            ? buildBankForm()
            : buildMercadoPagoForm()
        )

      ]
    );
    
  }


  Widget buildBankForm(){

    return Form(

      key: widget._formKey,

      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[

          TextFormField(
            decoration: InputDecoration(
              hintText: 'Nombre del medio de pago'
            ),
            maxLength: 20,
            validator: (s) => s.length == 0
              ? 'Debés elegir un nombre'
              : null,
            onSaved: (name) => _name=name,
          ),

          TextFormField(
            decoration: InputDecoration(
              hintText: 'CBU'
            ),
            maxLength: 22,
            keyboardType: TextInputType.number,
            inputFormatters: [
              WhitelistingTextInputFormatter.digitsOnly
            ],
            validator: (s) => ! RegExp("[0-9]{22}").hasMatch(s)
              ? 'CBU Inválido'
              : null,
            onSaved: (cbu) => _cbu=cbu,
          ),

          TextFormField(
            decoration: InputDecoration(
              hintText: 'CUIT/CUIL',
            ),
            maxLength: 11,
            keyboardType: TextInputType.number,
            inputFormatters: [
              WhitelistingTextInputFormatter.digitsOnly
            ],
            validator: (s) => ! RegExp("(20|23|24|27|30|33|34)[0-9]{8}[0-9]").hasMatch(s)
              ? 'CUIT/CUIL Inválido'
              : null,
            onSaved: (cuit) => _cuit=cuit,
          ),

          Padding(
            padding: const EdgeInsets.all(8.0),
            child: RaisedButton(
              child: Text('Agregar'),
              onPressed: (){
              
                if(! widget._formKey.currentState.validate())
                  return;

                widget._formKey.currentState.save();

                var bankAccount = BankAccount(
                  name: _name,
                  cbu: _cbu,
                  cuit: _cuit
                );

                Navigator.pop(context, bankAccount);
              
              }

            ),
          )

        ],
      ),
    );

  }


  Widget buildMercadoPagoForm(){

    final emailRegex = "^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,253}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,253}[a-zA-Z0-9])?)*\$";

    return Form(
      key: widget._formKey,
      child: Column(
        children: <Widget>[

          TextFormField(
            decoration: InputDecoration(
              hintText: 'Nombre del medio de pago'
            ),
            maxLength: 20,
            validator: (s) => s.length == 0
              ? 'Debés elegir un nombre'
              : null,
            onSaved: (name) => _name=name,
          ),

          TextFormField(
            decoration: InputDecoration(
              hintText: 'Email',
            ),
            validator: (s) => ! RegExp(emailRegex).hasMatch(s)
              ? 'Email Inválido'
              : null,
            onSaved: (email) => _email=email,
          ),

          Padding(
            padding: const EdgeInsets.all(8.0),
            child: RaisedButton(
              child: Text('Agregar'),
              onPressed: (){
              
                if(! widget._formKey.currentState.validate())
                  return;

                widget._formKey.currentState.save();

                var mpAccount = MercadoPagoAccount(
                  name: _name,
                  email: _email,
                );

                Navigator.pop(context, mpAccount);
              
              }

            ),
          )

        ],
      ),
    );

  }


}