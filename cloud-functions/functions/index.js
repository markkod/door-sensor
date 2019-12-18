const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();

exports.doorChanged = functions.database.ref('/doors/VhNuY5pnirysCIzhZkjT/isOpenState')
    .onUpdate((change, context) => {
        const original = change.after.val().toString();
        console.log(original);
        var payload = {
            data:{
              doorSate: original,
              registration_ids: ["e0qcsEJsGDQ:APA91bFgyBu2EsnEbq1_vMCKyT3-AoFIbxBBehC7GZXPsJNXd3sHWqFZXVC8hXMXhKsI1JmLKxb19tp6XlvDXJj-lb7O1HVmBosUgqQN2ggPhDxgbg2fimECLKxJc6gNurfvqUjrGw7E"]
            }
        };
        console.log("Sending message");
        return admin.messaging().sendToDevice("1:921772010269:android:2b942693d4e2987d4d4ff4", payload);
    })
