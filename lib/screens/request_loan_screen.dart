import 'package:avancer/models/user.dart';
import 'package:avancer/others/error_logger.dart';
import 'package:avancer/widgets/dots_loading_indicator.dart';
import 'package:avancer/models/loan.dart';
import 'package:avancer/others/utils.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:avancer/models/deposit_method.dart';
import 'package:avancer/screens/new_deposit_method_dialog.dart';
import 'package:sprintf/sprintf.dart';

class RequestLoanScreen extends StatefulWidget {

  final double amount;

  RequestLoanScreen(this.amount);

  @override
  _RequestLoanScreenState createState() => _RequestLoanScreenState();
}

class _RequestLoanScreenState extends State<RequestLoanScreen> {

  DepositMethod depositMethod;

  @override
  Widget build(BuildContext context) {

    Utils.checkInternetConnection(context);

    var loan = Loan(
      user: Provider.of<User>(context),
      amount: widget.amount,
      requestDate: DateTime.now(),
    );

    return Scaffold(

      appBar: AppBar(
        leading: Image.asset('assets/logo_small.png',),
        title: Text('Solicitar un Préstamo'),
        actions: <Widget>[
          IconButton(
            icon: Icon(Icons.arrow_back),
            onPressed: () => Navigator.of(context).pop(),
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
                          child: buildReviewLoanSection(context),
                        ),
                      ],
                    ),

                    buildDetailsSection(context, user, loan)

                  ],
                ),
              ),

              buildDepositMethodSection(context, user, loan),

              Padding(
                padding: const EdgeInsets.all(8.0),
                child: RaisedButton(
                  child: Text(
                    'Solicitar Retiro',
                    style: Theme.of(context).textTheme.button.copyWith(
                      fontSize: 22, color: Colors.white
                    ),
                  ),
                  onPressed: depositMethod != null 
                    ? () => requestLoan(context, user, loan)
                    : null,
                ),
              )
              
            ]
          ),
        ),
      )

    );
  }

  Widget buildReviewLoanSection(BuildContext context) {
    return Container(
      width: MediaQuery.of(context).size.width/2,
      child: Column(
        children: [
          
          Padding(
            padding: const EdgeInsets.fromLTRB(0, 32, 0, 8),
            child: Text(
              'Retirarás',
              style: Theme.of(context).textTheme.subtitle1.copyWith(color: Colors.white),
            ),
          ),

          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                Text(
                  '\$${widget.amount}',
                  style: Theme.of(context).textTheme.headline5.copyWith(
                    color: Colors.white
                  ),
                ),
              ],
            ),
          ),

        ]
      ),
    );
  }

  Widget buildDetailsSection(BuildContext context, User user, Loan loan) {

    return Align(
      alignment: Alignment.bottomCenter,
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[

            FutureBuilder(
              future: loan.fetchLimitDate(),
              builder: (context, snapshot){

                if(snapshot.connectionState != ConnectionState.done)
                  return DotsLoadingIndicator();

                if(snapshot.hasError)
                  return Text('Se produjo un error obteniendo el interés y la fecha de devolución');

                return Column(
                  children: [
                    Padding(
                      padding: const EdgeInsets.fromLTRB(8, 8, 8, 0),
                      child: Align(
                        alignment: Alignment.centerLeft,
                        child: Text(
                          'Deberás devolver',
                          style: Theme.of(context).textTheme.subtitle2.copyWith(fontWeight: FontWeight.normal),
                        )
                      ),
                    ),

                    Align(
                      alignment: Alignment.centerLeft,
                      child: Row(
                        children: <Widget>[
                          Padding(
                            padding: const EdgeInsets.fromLTRB(8, 8, 0, 0),
                            child: Text(
                              '${sprintf("\$%.2f", [loan.getDueOnLimit()])}',
                              style: Theme.of(context).textTheme.subtitle1.copyWith(fontWeight: FontWeight.bold),
                            ),
                          ),

                          IconButton(
                            icon: Icon(Icons.help, size: 18),
                            onPressed: () => showHelpDialog(context),
                          ),
                        ],
                      )
                    ),

                    Padding(
                      padding: const EdgeInsets.fromLTRB(8, 16, 8, 4),
                      child: Align(
                        alignment: Alignment.centerLeft,
                        child: Text(
                          'Antes de la fecha',
                          style: Theme.of(context).textTheme.subtitle2.copyWith(fontWeight: FontWeight.normal),
                        )
                      ),
                    ),

                    Padding(
                      padding: const EdgeInsets.fromLTRB(8, 4, 8, 8),
                      child: Align(
                        alignment: Alignment.centerLeft,
                        child: Text(
                          Utils.formatDate(loan.getLimitDate()),
                          style: Theme.of(context).textTheme.subtitle1.copyWith(fontWeight: FontWeight.bold),
                        )
                      ),
                    )
                  ]
                );
              },
            ),

            Padding(
              padding: const EdgeInsets.fromLTRB(8, 8, 8, 4),
              child: Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  'Tu nuevo monto disponible desde hoy será',
                  style: Theme.of(context).textTheme.subtitle2.copyWith(fontWeight: FontWeight.normal),
                )
              ),
            ),

            Padding(
              padding: const EdgeInsets.fromLTRB(8, 4, 8, 8),
              child: Align(
                alignment: Alignment.centerLeft,
                child: FutureBuilder<double>(
                  future: user.getAvailableLoan(),
                  builder: (context, snap){

                    if(snap.hasError){
                      return Text('No disponible');
                    }

                    if(snap.connectionState != ConnectionState.done)
                      return DotsLoadingIndicator();

                    return Text(
                      '${sprintf('\$%0.2f', [snap.data - loan.getAmount()])}',
                      style: Theme.of(context).textTheme.subtitle1.copyWith(fontWeight: FontWeight.bold),
                    );
                  }
                )
              )
            ),

          ]
        ),
      ),
    );
  }

  Widget buildDepositMethodSection(BuildContext context, User user, Loan loan) {

    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: FutureBuilder<List<DepositMethod>>(
        future: user.getDepositMethods(),
        builder: (context, snapshot){

          if(snapshot.hasError){
            Utils.showSnackbar('No se pudieron obtener tus medios de depósito', context);
            return Container();
          }

          if(snapshot.connectionState != ConnectionState.done){
            return DotsLoadingIndicator();
          }

          var depositMethods = snapshot.data;

          Map<DepositMethod, String> mappedDepositMethods = Map.fromEntries(
            depositMethods.map((method) => MapEntry(method, method.getName()))
          );

          return SizedBox(
            width: double.infinity,
            child: Card(
              child: Column(
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

                  depositMethods.isNotEmpty
                  ? Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: buildDepositMethodsDropDown(mappedDepositMethods)
                    )
                  : Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Text('Aún no agregaste ninguno'),
                    ),

                  Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      Padding(
                        padding: const EdgeInsets.all(4.0),
                        child: FlatButton(
                          child: Text(
                            'Agregar un medio',
                            style: Theme.of(context).textTheme.button.copyWith(color: Theme.of(context).primaryColor),
                          ),
                          onPressed: () => addDepositMethod(context, user)
                        ),
                      ),

                    ],
                  )

                ]
              ),
            ),
          );

        },
      ),
    );

  }

  Widget buildDepositMethodsDropDown(Map<DepositMethod, String> mappedDepositMethods) {

    return DropdownButtonFormField<DepositMethod>(
      key: GlobalKey<FormFieldState>(),

      icon: Icon(Icons.arrow_downward),
      iconSize: 24,
      elevation: 16,

      value: depositMethod,
      hint: Text('Tus medios'),

      items: mappedDepositMethods.entries.map((entry){
        return DropdownMenuItem<DepositMethod>(
          value: entry.key,
          child: Text(entry.value)
        );
      })?.toList(),
      
      onChanged: (DepositMethod newDepositMethod){
        setState((){ depositMethod = newDepositMethod; });
      },
    );
    
  }

  void showHelpDialog(BuildContext context){

    showDialog(
      context: context,
      child: AlertDialog(
        title: Text('¿Cómo se calcula el interés?'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('El interés es mensual, cuanto antes devuelvas el préstamo, menor será la comisión.' 
              + 'El interés se calcula en base a tu historial crediticio.'),
            Padding(
              padding: const EdgeInsets.symmetric(vertical: 16.0),
              child: Text('Tasa actual: ${Provider.of<User>(context, listen: false).getInterest() * 100}%',
                style: Theme.of(context).textTheme.bodyText1),
            )
          ],
        ),
      )
    );

  }

  Future<void> addDepositMethod(BuildContext context, User user) async {

    var newDepositMethod = await showDialog<DepositMethod>(
      context: context,
      builder: (context) => NewDepositMethodScreen()
    );

    if(newDepositMethod == null){
      return;
    }

    Utils.showSnackbar('Agregando medio', context);

    try{

      await user.addDepositMethod(newDepositMethod);

      Utils.showSnackbar('Medio agregado', context);

      setState(() {
        depositMethod = newDepositMethod;
      });

    }catch(error){

      ErrorLogger.log(context: 'Adding deposit method', error: error.toString(), userUID: user.getId());
      Utils.showSnackbar('Ha surgido un error', context);

    }

  }

  Future<void> requestLoan(BuildContext context, User user, Loan loan) async {

    bool accepted = await showDialog<bool>(
      barrierDismissible: false,
      context: context,
      builder: (context){
        return AlertDialog(
          title: Text('IMPORTANTE'),
          content: Text('Recordá que tenés hasta el ${Utils.formatDate(loan.getLimitDate())} para devolver el préstamo. '
           + 'Podrás hacerlo mediante transferencia bancaria o Rapipago.'),
           actions: <Widget>[
             FlatButton(
               child: Text('Aceptar'),
               onPressed: () => Navigator.of(context).pop(true),
             ),
             FlatButton(
               child: Text('Cancelar'),
               onPressed: () => Navigator.of(context).pop(false),
             )
           ],
        );
      }
    );

    if(!accepted){
      return;
    }

    showDialog(
      context: context,
      barrierDismissible: false,
      child: AlertDialog(
        title: Text('Enviando solicitud'),
        content: Text('Por favor aguardá'),
      )
    );

    bool approved = await user.requestLoan(widget.amount, depositMethod);

    Navigator.of(context).pop();

    if(approved){

      Navigator.of(context).pop(true);

    }else{

      Scaffold.of(context).showSnackBar(
        SnackBar(
          content: Text('Lo sentimos, ha ocurrido un error'),
          duration: Duration(seconds: 4),
        )
      );

    }
          
  }
}