const API_URL = "/clientes";

const formulario = document.getElementById("form-cliente");
const lista = document.getElementById("lista-clientes");
const mensagem = document.getElementById("mensagem");

const campoTipo = document.getElementById("tipo");
const campoCpf = document.getElementById("cpf");
const campoCnpj = document.getElementById("cnpj");

let clienteEditando = null;

function atualizarCamposDocumento() {
    const b2c = campoTipo.value === "B2C";

    document.getElementById("campo-cpf").hidden = !b2c;
    document.getElementById("campo-cnpj").hidden = b2c;

    campoCpf.required = b2c;
    campoCnpj.required = !b2c;
}

campoTipo.addEventListener("change", atualizarCamposDocumento);

async function carregarClientes() {
    lista.replaceChildren();

    try {
        const resposta = await fetch(API_URL);

        if (!resposta.ok) {
            throw new Error("Não foi possível carregar os clientes.");
        }

        const clientes = await resposta.json();

        if (clientes.length === 0) {
            mostrarMensagemTabela("Nenhum cliente cadastrado.");
            return;
        }

        clientes.forEach(cliente => {
            const linha = document.createElement("tr");
            const endereco = cliente.enderecos?.[0];

            const valores = [
                cliente.nome,
                cliente.tipo,
                cliente.email,
                cliente.telefone || "-",
                cliente.tipo === "B2C"
                    ? cliente.cpf || "-"
                    : cliente.cnpj || "-",
                endereco
                    ? `${endereco.cidade}/${endereco.uf}`
                    : "-"
            ];

            valores.forEach(valor => {
                const coluna = document.createElement("td");
                coluna.textContent = valor;
                linha.appendChild(coluna);
            });

            const acoes = document.createElement("td");

            const botaoEditar = document.createElement("button");
            botaoEditar.textContent = "Editar";
            botaoEditar.addEventListener("click", () => prepararEdicao(cliente));

            const botaoExcluir = document.createElement("button");
            botaoExcluir.textContent = "Excluir";
            botaoExcluir.addEventListener("click", () => excluirCliente(cliente));

            acoes.append(botaoEditar, botaoExcluir);
            linha.appendChild(acoes);
            lista.appendChild(linha);
        });

    } catch (erro) {
        mostrarMensagemTabela(erro.message);
    }
}

function mostrarMensagemTabela(texto) {
    lista.replaceChildren();

    const linha = document.createElement("tr");
    const coluna = document.createElement("td");

    coluna.colSpan = 7;
    coluna.textContent = texto;

    linha.appendChild(coluna);
    lista.appendChild(linha);
}

formulario.addEventListener("submit", async evento => {
    evento.preventDefault();

    const cliente = {
        tipo: campoTipo.value,
        nome: formulario.nome.value.trim(),
        email: formulario.email.value.trim(),
        telefone: formulario.telefone.value.trim(),
        cpf: campoCpf.value.trim() || null,
        cnpj: campoCnpj.value.trim() || null,
        enderecos: [{
            logradouro: formulario.logradouro.value.trim(),
            numero: formulario.numero.value.trim(),
            complemento: formulario.complemento.value.trim(),
            bairro: formulario.bairro.value.trim(),
            cidade: formulario.cidade.value.trim(),
            uf: formulario.uf.value.trim().toUpperCase(),
            cep: formulario.cep.value.trim()
        }]
    };

    try {
        const url = clienteEditando
            ? `${API_URL}/${clienteEditando}`
            : API_URL;

        const resposta = await fetch(url, {
            method: clienteEditando ? "PUT" : "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(cliente)
        });

        if (!resposta.ok) {
            const detalhe = await resposta.text();
            throw new Error(detalhe || "Erro ao salvar cliente.");
        }

        mensagem.textContent = clienteEditando
            ? "Cliente atualizado com sucesso!"
            : "Cliente cadastrado com sucesso!";

        limparFormulario();
        await carregarClientes();

    } catch (erro) {
        mensagem.textContent = erro.message;
    }
});

function prepararEdicao(cliente) {
    clienteEditando = cliente.id;

    formulario.tipo.value = cliente.tipo;
    formulario.nome.value = cliente.nome || "";
    formulario.email.value = cliente.email || "";
    formulario.telefone.value = cliente.telefone || "";
    campoCpf.value = cliente.cpf || "";
    campoCnpj.value = cliente.cnpj || "";

    const endereco = cliente.enderecos?.[0] || {};

    formulario.logradouro.value = endereco.logradouro || "";
    formulario.numero.value = endereco.numero || "";
    formulario.complemento.value = endereco.complemento || "";
    formulario.bairro.value = endereco.bairro || "";
    formulario.cidade.value = endereco.cidade || "";
    formulario.uf.value = endereco.uf || "";
    formulario.cep.value = endereco.cep || "";

    atualizarCamposDocumento();

    document.getElementById("titulo-formulario").textContent = "Editar cliente";
    document.getElementById("btn-salvar").textContent = "Salvar alterações";
    document.getElementById("btn-cancelar").hidden = false;

    formulario.scrollIntoView({behavior: "smooth"});
}

function limparFormulario() {
    formulario.reset();
    clienteEditando = null;

    document.getElementById("titulo-formulario").textContent = "Cadastrar cliente";
    document.getElementById("btn-salvar").textContent = "Cadastrar cliente";
    document.getElementById("btn-cancelar").hidden = true;

    atualizarCamposDocumento();
}

document.getElementById("btn-cancelar").addEventListener("click", () => {
    limparFormulario();
    mensagem.textContent = "";
});

async function excluirCliente(cliente) {
    if (!confirm(`Deseja excluir o cliente "${cliente.nome}"?`)) {
        return;
    }

    try {
        const resposta = await fetch(`${API_URL}/${cliente.id}`, {
            method: "DELETE"
        });

        if (!resposta.ok) {
            throw new Error("Não foi possível excluir o cliente.");
        }

        mensagem.textContent = "Cliente excluído com sucesso!";
        await carregarClientes();

    } catch (erro) {
        mensagem.textContent = erro.message;
    }
}

document.getElementById("btn-atualizar")
    .addEventListener("click", carregarClientes);

atualizarCamposDocumento();
carregarClientes();
