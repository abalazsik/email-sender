
const SERVER = "https://evidbihbo9.execute-api.eu-central-1.amazonaws.com/Stage"; // change this to your API url

//const SERVER = "http://127.0.0.1:3000";

export interface Contact {
    senderName: string,
    senderEmail: string,
    text: string,
    token: string
};

export function contact(cq: Contact) {
    return window.fetch(encodeURI(SERVER + "/contact"),
        {
            method: "post",
            body: JSON.stringify(cq),
            headers: {
                'Content-Type': 'application/json'
            }
        });
}