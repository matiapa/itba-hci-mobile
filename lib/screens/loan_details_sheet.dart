import 'package:avancer/models/loan.dart';
import 'package:avancer/others/remote_config_api.dart';
import 'package:avancer/others/utils.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:sprintf/sprintf.dart';

class LoanDetails extends StatelessWidget{
  
  final Loan loan;

  const LoanDetails(this.loan, {Key key}) : super(key: key);


  /* ------------------------------------------------------------------------------------------------------------
                                            BUILD METHODS
  ------------------------------------------------------------------------------------------------------------ */


  @override
  Widget build(BuildContext context) {

    Utils.checkInternetConnection(context);
    
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: ListView(
        shrinkWrap: true,
        children: [

          buildGeneralSection(context),

          buildDetailSection(context),

          buildActionSection(context)

        ]
      ),
    );
    
  }


  Widget buildGeneralSection(BuildContext context){

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [

        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Text(
            'ID: ${loan.getLoanId()}',
            style: Theme.of(context).textTheme.subtitle1.copyWith(fontWeight: FontWeight.bold)
          ),
        ),

        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Text('Monto retirado: \$${loan.getAmount()}'),
        ),

        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Text('Fecha de solicitud: ${Utils.formatDate(loan.getRequestDate())}'),
        ),

        Padding(
          padding: const EdgeInsets.all(8.0),
          child: Text('Estado: ${getStateDescription(loan)}'),
        ),

      ]
    );

  }


  Widget buildDetailSection(BuildContext context){

    switch(loan.getState()){
      
      case LoanState.GRANTED:
        return Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [

            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Divider(),
            ),

            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(sprintf('Monto a devolver \$%0.2f', [loan.getDueAmount()])),
            ),

            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text('Fecha límite de devolución: ${Utils.formatDate(loan.getDueDate())}'),
            )

          ]
        );

      case LoanState.REPAID:
        return Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [

            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Divider(),
            ),

            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(sprintf('Monto devuelto \$%0.2f', [loan.getDueAmount()])),
            )

          ]
        );

      default:
        return Container();

    }

  }


  Widget buildActionSection(BuildContext context){

    switch(loan.getState()){

      case LoanState.GRANTED:
        return Column(
          children: [

            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Divider(),
            ),

            FlatButton(
              child: Text('Pagar con transferencia'),
              textColor: Theme.of(context).primaryColor,
              onPressed: () => showBankPaymentDialog(context),
            ),

            FlatButton(
              child: Text('Pagar con Mercadopago'),
              textColor: Theme.of(context).primaryColor,
              onPressed: () => showMPPaymentDialog(context),
            ),

            FlatButton(
              child: Text('Realizar un reclamo'),
              textColor: Theme.of(context).primaryColor,
              onPressed: () => showClaimDialog(context),
            )

          ]
        );

      default:
        return Column(
          children: [

            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Divider(),
            ),

            FlatButton(
              child: Text('Realizar un reclamo'),
              textColor: Theme.of(context).primaryColor,
              onPressed: () => showClaimDialog(context),
            )

          ]
        );

    }

  }


  /* ------------------------------------------------------------------------------------------------------------
                                            ACTION METHODS
  ------------------------------------------------------------------------------------------------------------ */


  String getStateDescription(Loan loan){

    switch(loan.getState()){

      case LoanState.REQUESTED:
        return 'Solicitado, aún no entregado';

      case LoanState.GRANTED:
        return 'Entregado, devolución pendiente';

      case LoanState.REPAID:
        return 'Devuelto';

      default:
        return '';
    }

  }


  void showClaimDialog(BuildContext context){

    showDialog(
      context: context,
      child: Utils.buildColumnDialog(
        children: <Widget>[
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Text('Para realizar un reclamo enviá un mail a:'),
          ),
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextFormField(
              initialValue: RemoteConfigApi.instance().contactEmail,
              readOnly: true,
            ),
          ),
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Text('Incluí el siguiente ID de préstamo:'),
          ),
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextFormField(
              initialValue: loan.getLoanId(),
              readOnly: true,
            ),
          )
        ]
      )
    );

  }


  void showBankPaymentDialog(BuildContext context){

    showDialog(
      context: context,
      child: Utils.buildColumnDialog(
        children: <Widget>[

          Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextFormField(
              initialValue: sprintf('\$%0.2f', [loan.getDueAmount()]),
              decoration: InputDecoration(labelText: 'Monto a pagar'),
              readOnly: true,
            ),
          ),

          Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextFormField(
              initialValue: RemoteConfigApi.instance().bankCBU,
              decoration: InputDecoration(labelText: 'CBU'),
              readOnly: true,
            ),
          ),

          Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextFormField(
              initialValue: RemoteConfigApi.instance().bankAlias,
              decoration: InputDecoration(labelText: 'Alias'),
              readOnly: true,
            ),
          ),

          Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextFormField(
              initialValue: loan.getLoanId(),
              decoration: InputDecoration(labelText: 'Referencia'),
              readOnly: true,
            ),
          ),

          Padding(
            padding: const EdgeInsets.fromLTRB(8, 16, 8, 16),
            child: Text(
              'IMPORTANTE:'
              +'\n- Incluí el código de referencia.'
              +'\n- Tomará hasta 48hs procesar tu pago.',
              style: Theme.of(context).textTheme.bodyText1,
            ),
          ),

        ]
      )
    );

  }


  void showMPPaymentDialog(BuildContext context){

    showDialog(
      context: context,
      child: Utils.buildColumnDialog(
        children: <Widget>[

          Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextFormField(
              initialValue: sprintf('\$%0.2f', [loan.getDueAmount()]),
              decoration: InputDecoration(labelText: 'Monto a pagar'),
              readOnly: true,
            ),
          ),

          Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextFormField(
              initialValue: RemoteConfigApi.instance().mpEmail,
              decoration: InputDecoration(labelText: 'Email de MercadoPago'),
              readOnly: true,
            ),
          ),

          Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextFormField(
              initialValue: loan.getLoanId(),
              decoration: InputDecoration(labelText: 'Referencia'),
              readOnly: true,
            ),
          ),

          Padding(
            padding: const EdgeInsets.fromLTRB(8, 16, 8, 16),
            child: Text(
              'IMPORTANTE:'
              +'\n- Incluí el código de referencia.'
              +'\n- Tomará hasta 48hs procesar tu pago.',
              style: Theme.of(context).textTheme.bodyText1,
            ),
          ),

        ]
      )
    );

  }


}