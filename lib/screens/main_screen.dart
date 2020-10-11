import 'package:avancer/models/loan.dart';
import 'package:avancer/models/user.dart';
import 'package:avancer/others/remote_config_api.dart';
import 'package:avancer/screens/loan_details_sheet.dart';
import 'package:avancer/widgets/dots_loading_indicator.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';
import 'package:sprintf/sprintf.dart';
import 'package:avancer/others/utils.dart';

class MainScreen extends StatelessWidget {

  /* ----------------------------------------------------------------------------------------------------------------------
                                                    BUILD METHODS
  ---------------------------------------------------------------------------------------------------------------------- */

  @override
  Widget build(BuildContext context) {

    Utils.checkInternetConnection(context);

    return Scaffold(

      appBar: AppBar(
        leading: Image.asset('assets/logo_small.png'),
        title: Text('Avancer'),
        actions: <Widget>[
          IconButton(
            icon: Icon(Icons.person),
            onPressed: (){
              Navigator.of(context).pushNamed('/profile');
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

                        buildRequestLoanSection(context, user)
                      ],
                    ),

                    buildResumeSection(context, user)

                  ],
                ),
              ),

              Padding(
                padding: const EdgeInsets.symmetric(vertical: 8.0),
              ),

              buildPastLoansCard(context, user)
              
            ]
          ),
        ),
      )

    );
  }


  Widget buildRequestLoanSection(BuildContext context, User user) {

    var _inputController = TextEditingController();
    _inputController.text = '0.0';

    return Container(
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [

          Expanded(
            child: Container(
              padding: const EdgeInsets.fromLTRB(8, 24, 8, 24),
              child: TextFormField(
                controller: _inputController,
                decoration: InputDecoration(
                  fillColor: Colors.white,
                  prefix: Text(
                    '\$ ',
                    style: Theme.of(context).textTheme.headline5.copyWith(
                      color: Colors.white
                    )
                  ),
                  enabledBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Colors.white)
                  ),
                  focusedBorder: OutlineInputBorder(
                    borderSide: BorderSide(color: Colors.white)
                  )
                ),
                cursorColor: Colors.white,
                style: Theme.of(context).textTheme.headline5.copyWith(
                  color: Colors.white
                ),
                keyboardType: TextInputType.number,
                inputFormatters: [
                  WhitelistingTextInputFormatter.digitsOnly
                ],
              ),
            ),
          ),

          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Container(
              height: 50,
              child: FloatingActionButton(
                child: Icon(
                  Icons.attach_money,
                  color: Colors.black,
                ),
                backgroundColor: Colors.white,
                onPressed: ()async {
                  requestLoan(_inputController.text, context, user);
                }
              ),
            ),
          )
          
        ]
      ),
    );
  }


  Widget buildResumeSection(BuildContext context, User user) {

    return Align(
      alignment: Alignment.bottomCenter,
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[

            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(
                '¡Hola ${user.getName()}!',
                style: Theme.of(context).textTheme.headline6,
              ),
            ),

            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(
                'Resumen de tu cuenta',
                style: Theme.of(context).textTheme.subtitle1,
              ),
            ),

            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Row(
                children: <Widget>[
                  
                  Expanded(
                    child: Container(
                      height: 100,
                      color: Color.fromRGBO(56,39,180,1),
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: <Widget>[

                          Text(
                            'Disponible para retiro',
                            style: Theme.of(context).textTheme.bodyText2.copyWith(color: Colors.white),
                            textAlign: TextAlign.center,
                          ),

                          Row(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: <Widget>[
                              Padding(
                                padding: const EdgeInsets.fromLTRB(16,0,8,0),
                                child: FutureBuilder<double>(
                                  future: user.getAvailableLoan(),
                                  builder: (context, snap){
                                    if(snap.connectionState != ConnectionState.done)
                                      return DotsLoadingIndicator();

                                    return Text(
                                      sprintf("\$%.0f", [snap.data]),
                                      style: Theme.of(context).textTheme.subtitle1.copyWith(color: Colors.white),
                                    );
                                  },
                                ),
                              ),

                              GestureDetector(
                                  child: Icon(Icons.help, size: 18, color: Colors.white),
                                  onTap: () => showAvailableAmountHelpDialog(context)
                              )
                            ],
                          ),

                        ],
                      ),
                    )
                  ),

                  Padding(
                    padding: const EdgeInsets.all(8.0),
                  ),

                  Expanded(
                    child: Container(
                      height: 100,
                      color: Color.fromRGBO(224,139,182,1),
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: <Widget>[

                          Text(
                            'Total a devolver',
                            style: Theme.of(context).textTheme.bodyText2.copyWith(color: Colors.white),
                            textAlign: TextAlign.center,
                          ),

                          FutureBuilder<double>(
                            future: getDebt(user),
                            builder: (context, snap){

                              if(snap.hasError){
                                return Text('No disponible');
                              }

                              if(snap.connectionState != ConnectionState.done)
                                return DotsLoadingIndicator();

                              return Text(
                                sprintf("\$%.0f", [snap.data]),
                                style: Theme.of(context).textTheme.subtitle1.copyWith(color: Colors.white),
                              );
                            },
                          )

                        ],
                      )
                    )
                  )

                ],
              ),
            )

          ]
        ),
      ),
    );
  }


  Widget buildPastLoansCard(BuildContext context, User user) {

    return SizedBox(
      width: double.infinity,
      child: Card(
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: FutureBuilder<List<Loan>>(
            future: getLatestLoans(user, context),
            builder: (context, snap) {

              if(snap.hasError){
                return Container();
              }

              if(snap.connectionState != ConnectionState.done){
                return DotsLoadingIndicator();
              }

              var latestLoans = snap.data;

              if(latestLoans.isEmpty){
                return Column(
                  children: [

                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Align(
                        alignment: Alignment.centerLeft,
                        child: Text(
                          'Retiros pasados',
                          style: Theme.of(context).textTheme.subtitle2,
                        )
                      ),
                    ),

                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Text(
                        'Aún no has realizado ningún retiro. Utilizá la barra superior para realizar uno.',
                        textAlign: TextAlign.center,
                      ),
                    )

                  ]
                );
              }
              
              return Column(
                children: [

                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Align(
                      alignment: Alignment.centerLeft,
                      child: Text(
                        'Retiros pasados',
                        style: Theme.of(context).textTheme.headline6,
                      )
                    ),
                  ),

                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Align(
                      alignment: Alignment.centerLeft,
                      child: Text(
                        'Presioná para ver opciones de pago. Podés devolver todos tus préstamos en'
                         +' una sola transferencia',
                        style: Theme.of(context).textTheme.subtitle2,
                      )
                    ),
                  ),

                  buildListTile(user, latestLoans.length>0 ? latestLoans[0] : null, context),

                  buildListTile(user, latestLoans.length>1 ? latestLoans[1] : null, context),

                  buildListTile(user, latestLoans.length>2 ? latestLoans[2] : null, context),

                  buildListTile(user, latestLoans.length>3 ? latestLoans[3] : null, context),

                  FlatButton(
                    child: Text('VER TODOS'),
                    textColor: Theme.of(context).primaryColor,
                    onPressed: (){
                      Navigator.of(context).pushNamed('/allLoans');
                    },
                  )

                ]
              );

            },
          ),
        )
      ),
    );

  }


  Widget buildListTile(User user, Loan loan, BuildContext context) {

    if(loan == null)
      return Container();

    return Column(
      children: <Widget>[
        ListTile(
          leading: Icon(
            Icons.attach_money,
          ),

          title: Text(
            getStateDescription(loan)
          ),

          trailing: Text(
            sprintf("\$%.0f", [loan.getAmount()]),
            style: Theme.of(context).textTheme.subtitle1.copyWith(color: Theme.of(context).primaryColor),
          ),

          onTap: () => showModalBottomSheet(
            context: context,
            builder: (context) => LoanDetails(loan)
          ),
        ),

        Divider(),
      ],
    );

  }


  /* ------------------------------------------------------------------------------------------------------------
                                            ACTIONS METHODS
  ------------------------------------------------------------------------------------------------------------ */


  void showAvailableAmountHelpDialog(BuildContext context){

    var rc = RemoteConfigApi.instance();

    showDialog(
      context: context,
      child: AlertDialog(
        title: Text('¿Cómo se calcula el límite?'),
        content: Text('Todos los meses podés retirar el monto equivalente al ${rc.salaryFraction*100}% de los días que trabajaste. '
          + 'Cada día que trabajes, tu monto disponible va a aumentar'),
      )
    );

  }


  Future<double> getDebt(User user) async {

    var pastLoans = await user.getPastLoans();

    if(pastLoans.isEmpty)
      return 0;
   
    var selectedLoans = pastLoans.where((loan) => 
      loan.getState() == LoanState.GRANTED
    );

    if(selectedLoans.isEmpty)
      return 0;

    return selectedLoans
      .map((loan) => loan.getDueAmount() as double)
      .reduce((amount1, amount2) => amount1 + amount2);

  }


  Future<List<Loan>> getLatestLoans(User user, BuildContext context) async{

    try{
      
      var loans = await user.getPastLoans();
    
      loans.sort();

      return loans;

    }catch(e){

      Utils.showSnackbar('No se pudo obtener tus últimos préstamos', context);

      return null;

    }

  }


  String getStateDescription(Loan loan){

    try{

      var loanState = loan.getState();

      switch(loanState){

        case LoanState.REQUESTED:
          return sprintf('Solicitado el %s', [Utils.formatDate(loan.getRequestDate())]);

        case LoanState.GRANTED:
          return sprintf('Debes devolverlo el %s', [Utils.formatDate(loan.getDueDate())]);

        case LoanState.REPAID:
          return 'Préstamo devuelto';

        default:
          return '';

      }

    }catch(e){

      return 'Se produjo un error al cargar este préstamo';

    }

  }


  void requestLoan(String amountStr, BuildContext context, User user) async {
    
    if(amountStr.length == 0){
      Utils.showSnackbar('Por favor, ingresá una cantidad', context);
      return;
    }

    double amount = double.parse(amountStr);
    var rc = RemoteConfigApi.instance();

    if(amount < rc.minLoanAmount){
      Utils.showSnackbar('El monto mínimo es de \$${RemoteConfigApi.instance().minLoanAmount}', context);
      return;
    }

    if(amount > await user.getAvailableLoan()){
      Utils.showSnackbar('El monto está por encima de tu disponible', context);
      return;
    }    

    bool approved = await Navigator.of(context).pushNamed<bool>(
      '/requestLoan',
      arguments: {'amount': amount}
    );

    // If the request was not approved it wont pop, so if approved is not null is because the it was approved

    if(approved != null){
      Utils.showSnackbar('Solicitud realizada', context);
      return;
    }

  }


}