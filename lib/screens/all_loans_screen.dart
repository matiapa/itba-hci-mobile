import 'package:avancer/models/loan.dart';
import 'package:avancer/models/user.dart';
import 'package:avancer/others/utils.dart';
import 'package:avancer/widgets/dots_loading_indicator.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:sprintf/sprintf.dart';
import 'package:date_range_picker/date_range_picker.dart' as DateRagePicker;

import 'loan_details_sheet.dart';

class AllLoansScreen extends StatefulWidget {

  @override
  _AllLoansScreenState createState() => _AllLoansScreenState();

}

class _AllLoansScreenState extends State<AllLoansScreen> {

  DateTime startDate = DateTime.now().subtract(Duration(days: 7));
  DateTime endDate = DateTime.now();


  /* ------------------------------------------------------------------------------------------------------------
                                            BUILD METHODS
  ------------------------------------------------------------------------------------------------------------ */
  

  @override
  Widget build(BuildContext context) {

    Utils.checkInternetConnection(context);

    var user = Provider.of<User>(context, listen: false);

    return Scaffold(

      appBar: AppBar(
        leading: Image.asset('assets/logo_small.png',),
        title: Text('Todos tus préstamos'),
        actions: <Widget>[
          IconButton(
            icon: Icon(Icons.arrow_back),
            onPressed: () => Navigator.of(context).pop(),
          )
        ],
      ),

      body: Container(
        child: Column(
          children: <Widget>[

            Stack(
              alignment: Alignment.topCenter,
              children: <Widget>[

                Image.asset(
                  'assets/waves_top.png',
                  width: MediaQuery.of(context).size.width,
                ),

                Container(
                  padding: const EdgeInsets.symmetric(vertical: 32),
                  width: MediaQuery.of(context).size.width*0.7,
                  child: TextField(
                    decoration: InputDecoration(
                      hintText: sprintf("%s - %s", [Utils.formatDate(startDate), Utils.formatDate(endDate)]),
                      hintStyle: Theme.of(context).textTheme.headline5.copyWith(
                        color: Colors.white
                      ),
                      enabledBorder: OutlineInputBorder(      
                        borderSide: BorderSide(color: Colors.white),   
                      ),  
                      focusedBorder: OutlineInputBorder(
                        borderSide: BorderSide(color: Colors.white),   
                      ),
                    ),
                    cursorColor: Colors.white,
                    style: Theme.of(context).textTheme.headline5.copyWith(
                      color: Colors.white
                    ),
                    readOnly: true,
                    onTap: () async{

                      final List<DateTime> picked = await DateRagePicker.showDatePicker(
                          context: context,
                          initialFirstDate: startDate,
                          initialLastDate: endDate,
                          firstDate: new DateTime(2015),
                          lastDate: new DateTime(2021),
                      );

                      if(picked!=null){
                        setState((){

                          startDate = picked[0];

                          endDate = picked.length==2 ? picked[1].add(Duration(hours: 23, minutes: 59)) : DateTime.now();

                        });
                      }

                    },
                  ),
                )

              ],
            ),

            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(
                'Presioná un préstamo para ver opciones de pago',
                style: Theme.of(context).textTheme.subtitle1,
              ),
            ),

            Expanded(
              child: FutureBuilder<List<Loan>>(
                future: getSelectedLoans(user),
                builder: (context, snap){

                  if(snap.hasError){
                    return Container();
                  }

                  if(snap.connectionState != ConnectionState.done){
                    return DotsLoadingIndicator();
                  }

                  var selectedLoans = snap.data;

                  if(selectedLoans.isEmpty){
                    return Center(
                      child: Text(
                        'No se realizó ningún préstamo en el período seleccionado',
                        textAlign: TextAlign.center,
                        style: Theme.of(context).textTheme.headline6,
                      ),
                    );
                  }

                  return ListView.separated(
                    shrinkWrap: true,
                    itemBuilder: (context, i) => buildListTile(user, selectedLoans[i], context),
                    separatorBuilder: (context, i) => Divider(),
                    itemCount: selectedLoans.length
                  );

                },
              ),
            ),

          ],
        ),
      ),
    );

  }


  Widget buildListTile(User user, Loan loan, BuildContext context) {

    return ListTile(
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
    );

  }


  /* ------------------------------------------------------------------------------------------------------------
                                            ACTION METHODS
  ------------------------------------------------------------------------------------------------------------ */


  Future<List<Loan>> getSelectedLoans(User user) async {

    startDate = DateTime.utc(startDate.year, startDate.month, startDate.day, 0, 0, 0);
    endDate = DateTime.utc(endDate.year, endDate.month, endDate.day, 23, 59, 59);

    try{

      var loans = await user.getPastLoans();
      loans.sort();

      loans.forEach((loan) {print("Loan "+loan.getRequestDate().toIso8601String());});

      return loans.where((loan) => 
        loan.getRequestDate().isAfter(startDate) && loan.getRequestDate().isBefore(endDate)
      ).toList();

    }catch(e){

      Utils.showSnackbar('Se produjo un error obteniendo tus préstamos', context);
      
      return null;

    }

  }


  String getStateDescription(Loan loan){

    switch(loan.getState()){

      case LoanState.REQUESTED:
        return sprintf('Solicitado el %s', [Utils.formatDate(loan.getRequestDate())]);

      case LoanState.GRANTED:
        return sprintf('Debes devolverlo el %s', [Utils.formatDate(loan.getDueDate())]);

      case LoanState.REPAID:
        return 'Préstamo devuelto';

      default:
        return '';
    }

  }


}