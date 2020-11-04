<#assign charset="UTF-8">
<#assign title="Dashboard">

<#import "/globalTemplate.ftl" as globalTemplate>

<!DOCTYPE html>
<html lang="it">
<head>
    <title>${title} - PollWeb</title>
    <meta charset="${charset}">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <@globalTemplate.style />
</head>
<body>
<@globalTemplate.navbar />

<div class="bg-light pt-5 pb-5">
    <div class="container">
        <#if success ??>
            <@globalTemplate.success success />
        </#if>

        <#if error ??>
            <@globalTemplate.error error />
        </#if>
        <div class="card">
            <div class="card-body">
                <div class="row">
                    <div class="col-md-8">
                        <h1 class="text-primary">Lista sondaggi</h1>
                    </div>
                    <div class="col-md-4">
                        <div class="float-right">
                            <a href="/sondaggi/crea_sondaggio" class="btn btn-primary"><i class="fas fa-plus"></i> Nuovo sondaggio</a>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <#list sondaggi as sondaggio>
                        <div class="col-md-4 mt-2">
                            <div class="card">
                                <div class="card-body">
                                    <h5 class="card-title">${sondaggio.getTitolo()}</h5>
                                    <p><i class="fad fa-eye fa-fw"></i> ${sondaggio.getNomeVisibilita()}</p>
                                    <p><i class="fad fa-toggle-on fa-fw"></i> ${sondaggio.getNomeStato()}</p>
                                    <p>
                                        <#if sondaggio.getStato() == 0>
                                            <a href="/sondaggi/riepilogo?id=${sondaggio.getId()}" class="btn btn-primary">Vai al riepilogo</a>
                                        </#if>
                                        <#if sondaggio.getStato() == 1>
                                            <a href="/sondaggi/chiudi?id=${sondaggio.getId()}" class="btn btn-primary">Chiudi</a>
                                        </#if>
                                        <#if sondaggio.getStato() == 2>
                                            <a href="/sondaggi/risultato?id=${sondaggio.getId()}" class="btn btn-primary">Visualizza risultati</a>
                                            <a href="/sondaggi/esportazione?id=${sondaggio.getId()}" class="btn btn-secondary">CSV</a>
                                        </#if>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </#list>
                </div>
            </div>
        </div>
        <div class="card mt-3">
            <div class="card-body">
                <div class="col-md-8">
                    <h2 class="text-primary">Lista responsabili</h2>
                </div>
                <div class="col-md-4">
                    <a href="/utenti/nuovo_responsabile" class="btn btn-primary"><i class="fas fa-plus"></i> Nuovo responsabile</a>
                </div>
                <div class="row">
                    <#list responsabili as responsabile>
                        <div class="col-md-4 mt-2">
                            <div class="card">
                                <div class="card-body">
                                    <h5 class="card-title">${responsabile.getNome()} ${responsabile.getCognome()}</h5>
                                </div>
                            </div>
                        </div>
                    </#list>
                </div>
            </div>
        </div>

    </div>
</div>

<@globalTemplate.script />
</body>
</html>
